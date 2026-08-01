package com.snek.engineersbliss.client.utils.textures.svg;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.view.ViewBox;
import com.mojang.blaze3d.platform.NativeImage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
















public final class SvgRasterizer {
    private SvgRasterizer() {}

    private static final SVGLoader LOADER         = new SVGLoader();
    private static final String    SVG_NS         = "http://www.w3.org/2000/svg";
    private static final String    XLINK_NS       = "http://www.w3.org/1999/xlink";
    private static final Pattern   OPACITY_STYLE  = Pattern.compile("(?:^|;)\\s*opacity\\s*:\\s*([0-9.]+)");
    private static final Pattern   LENGTH_PATTERN = Pattern.compile("[0-9.]+");


    private static final String[] INHERITABLE_ATTRS = {
        "fill", "stroke", "fill-opacity", "stroke-opacity", "fill-rule",
        "stroke-width", "stroke-linecap", "stroke-linejoin", "stroke-dasharray", "color"
    };




    /**
     * Rasterizes an SVG image into a NativeImage.
     * @param svgBytes The SVG image as an array of bytes.
     * @param width The width of the target bitmap.
     * @param height The height of the target bitmap.
     * @return The NativeImage containing the rasterized SVG.
     */
    public static NativeImage rasterize(final byte[] svgBytes, final int width, final int height) {
        final byte[] flattened = flattenOpacityGroups(svgBytes, width, height);

        final SVGDocument doc;
        try {
            doc = loadSvg(flattened);
        }
        catch (final Exception e) {
            throw new RuntimeException("Failed to read SVG stream", e);
        }
        if(doc == null) {
            throw new RuntimeException("Failed to parse SVG");
        }

        return toNativeImage(renderToImage(doc, width, height));
    }








    /**
     * JSVG doesn't handle group transparency properly.
     * Instead of computing it on the flattened group, it multiplies it with the transparency of individual elements.
     * This makes the fill and outline colors of transparent elements overlap.
     *
     * This function fixes it by walking the SVG tree strarting from the bottom, rasterizing any non-opaque element in isolation
     * and storing the final opacity value in the raw pixels.
     * @param svgBytes
     * @param width
     * @param height
     * @return
     */
    private static byte[] flattenOpacityGroups(final byte[] svgBytes, final int width, final int height) {
        final Document doc;
        try {
            doc = parseXml(svgBytes);
        }
        catch(final Exception _) {
            // Bad file, return the original raw bytes. JSVG will report the real error
            return svgBytes;
        }

        final Element root = doc.getDocumentElement();
        flattenRecursive(doc, root, root, width, height);

        try {
            return serialize(doc);
        }
        catch(final Exception e) {
            throw new RuntimeException("Failed to serialize flattened SVG", e);
        }
    }


    private static void flattenRecursive(final Document doc, final Element root, final Element node, final int width, final int height) {
        final List<Element> children = new ArrayList<>();
        Node child = node.getFirstChild();
        while(child != null) {
            if(child.getNodeType() == Node.ELEMENT_NODE) children.add((Element) child);
            child = child.getNextSibling();
        }

        for(final Element c : children) {
            final String tag = c.getLocalName() != null ? c.getLocalName() : c.getTagName();
            if("defs".equals(tag) || "style".equals(tag) || "metadata".equals(tag)) continue;
            flattenRecursive(doc, root, c, width, height);
        }

        if(node == root) return;

        final float opacity = parseOpacity(node);
        if(opacity >= 1f) return;

        final byte[] isolated = rasterizeIsolatedSubtree(node, root, width, height, opacity);
        final ViewBoxInfo vb = ViewBoxInfo.of(root);

        final Element image = doc.createElementNS(SVG_NS, "image");
        image.setAttribute("x", String.valueOf(vb.minX));
        image.setAttribute("y", String.valueOf(vb.minY));
        image.setAttribute("width", String.valueOf(vb.width));
        image.setAttribute("height", String.valueOf(vb.height));
        image.setAttribute("preserveAspectRatio", "none");
        image.setAttributeNS(XLINK_NS, "xlink:href", "data:image/png;base64," + Base64.getEncoder().encodeToString(isolated));
        node.getParentNode().replaceChild(image, node);
    }








    /**
     * This builds a small SVG file with the same viewBox and defs as the root and rasterizes it in isolation.
     */
    private static byte[] rasterizeIsolatedSubtree(final Element node, final Element root,
            final int width, final int height, final float opacity) {
        try {
            final Document miniDoc = newDocumentBuilder().newDocument();

            final Element miniRoot = miniDoc.createElementNS(SVG_NS, "svg");
            miniRoot.setAttribute("viewBox", root.getAttribute("viewBox"));
            miniRoot.setAttribute("width", root.getAttribute("width"));
            miniRoot.setAttribute("height", root.getAttribute("height"));
            miniDoc.appendChild(miniRoot);

            final Node defs = findDirectChild(root, "defs");
            if(defs != null) miniRoot.appendChild(miniDoc.importNode(defs, true));

            final Element wrapper = miniDoc.createElementNS(SVG_NS, "g");
            applyAncestorContext(root, node, wrapper);
            miniRoot.appendChild(wrapper);

            final Element nodeCopy = (Element) miniDoc.importNode(node, true);
            nodeCopy.removeAttribute("opacity");
            stripStyleOpacity(nodeCopy);
            wrapper.appendChild(nodeCopy);

            final SVGDocument svg = loadSvg(serialize(miniDoc));
            if(svg == null) throw new RuntimeException("Failed to parse isolated subtree");

            final BufferedImage buf = renderToImage(svg, width, height);
            if(opacity < 1f) scaleAlpha(buf, opacity);

            final ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
            ImageIO.write(buf, "png", pngOut);
            return pngOut.toByteArray();
        }
        catch (final Exception e) {
            throw new RuntimeException("Failed to rasterize isolated subtree", e);
        }
    }


    private static DocumentBuilder newDocumentBuilder() throws Exception {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        return dbf.newDocumentBuilder();
    }


    private static Document parseXml(final byte[] bytes) throws Exception {
        return newDocumentBuilder().parse(new ByteArrayInputStream(bytes));
    }


    private static byte[] serialize(final Document doc) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(out));
        return out.toByteArray();
    }


    private static SVGDocument loadSvg(final byte[] svgBytes) throws Exception {
        try (InputStream is = new ByteArrayInputStream(svgBytes)) {
            return LOADER.load(is, null, LoaderContext.createDefault());
        }
    }


    private static BufferedImage renderToImage(final SVGDocument svg, final int width, final int height) {
        final BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        svg.render(null, g, new ViewBox(0, 0, width, height));
        g.dispose();
        return buf;
    }


    /**
     * Scales every pixel's alpha by the provided opacity
     */
    private static void scaleAlpha(final BufferedImage img, final float opacity) {
        final int w = img.getWidth(), h = img.getHeight();
        final int[] row = new int[w];
        for(int y = 0; y < h; y++) {
            img.getRGB(0, y, w, 1, row, 0, w);
            for(int x = 0; x < w; x++) {
                final int argb = row[x];
                final int a = (argb >>> 24) & 0xFF;
                final int newA = Math.round(a * opacity);
                row[x] = (newA << 24) | (argb & 0x00FFFFFF);
            }
            img.setRGB(0, y, w, 1, row, 0, w);
        }
    }

    /**
     * Collects the transform and attrs from the ancestors of the provided node and applies them to the wrapper.
     */
    private static void applyAncestorContext(final Element root, final Element node, final Element wrapper) {
        final List<Element> chain = new ArrayList<>();
        Node n = node.getParentNode();
        while(n instanceof Element e && n != root) {
            chain.add(0, e);
            n = n.getParentNode();
        }

        final StringBuilder transform = new StringBuilder();
        for(final Element ancestor : chain) {
            final String t = ancestor.getAttribute("transform");
            if(!t.isEmpty()) transform.append(t).append(' ');
            for(final String attr : INHERITABLE_ATTRS) {
                final String v = ancestor.getAttribute(attr);
                if(!v.isEmpty()) wrapper.setAttribute(attr, v);
            }
        }
        if(!transform.isEmpty()) wrapper.setAttribute("transform", transform.toString().trim());
    }


    private static void stripStyleOpacity(final Element el) {
        final String style = el.getAttribute("style");
        if(!style.isEmpty()) {
            el.setAttribute("style", OPACITY_STYLE.matcher(style).replaceAll(""));
        }
    }


    private static Node findDirectChild(final Element parent, final String tagName) {
        Node c = parent.getFirstChild();
        while(c != null) {
            if(c.getNodeType() == Node.ELEMENT_NODE) {
                final Element ce = (Element) c;
                final String tag = ce.getLocalName() != null ? ce.getLocalName() : ce.getTagName();
                if(tagName.equals(tag)) return c;
            }
            c = c.getNextSibling();
        }
        return null;
    }


    private static float parseOpacity(final Element el) {
        final String attr = el.getAttribute("opacity");
        if(!attr.isEmpty()) {
            return clamp01(parseFloatSafe(attr));
        }
        final String style = el.getAttribute("style");
        if(!style.isEmpty()) {
            final Matcher m = OPACITY_STYLE.matcher(style);
            if(m.find()) return clamp01(parseFloatSafe(m.group(1)));
        }
        return 1f;
    }


    private static float parseFloatSafe(final String s) {
        try {
            return Float.parseFloat(s.trim());
        }
        catch(final Exception _) {
            return 1f;

        }
    }


    private static float clamp01(final float v) {
        return v < 0f ? 0f : Math.min(v, 1f);
    }


    private static double parseLength(final String s) {
        if(s == null || s.isEmpty()) return 0;
        final Matcher m = LENGTH_PATTERN.matcher(s);
        return m.find() ? Double.parseDouble(m.group()) : 0;
    }


    private static final class ViewBoxInfo {
        final double minX;
        final double minY;
        final double width;
        final double height;

        private ViewBoxInfo(final double minX, final double minY, final double width, final double height) {
            this.minX = minX;
            this.minY = minY;
            this.width = width;
            this.height = height;
        }

        static ViewBoxInfo of(final Element root) {
            final String vb = root.getAttribute("viewBox");
            if(!vb.isEmpty()) {
                final String[] p = vb.trim().split("\\s+");
                return new ViewBoxInfo(Double.parseDouble(p[0]), Double.parseDouble(p[1]),
                    Double.parseDouble(p[2]), Double.parseDouble(p[3]));
            }
            return new ViewBoxInfo(0, 0, parseLength(root.getAttribute("width")), parseLength(root.getAttribute("height")));
        }
    }


    private static NativeImage toNativeImage(final BufferedImage img) {
        final int w = img.getWidth(), h = img.getHeight();
        final NativeImage out = new NativeImage(w, h, false);
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                final int argb = img.getRGB(x, y);
                final int a  = (argb >>> 24) & 0xFF;
                final int r  = (argb >>> 16) & 0xFF;
                final int gC = (argb >>>  8) & 0xFF;
                final int b  =  argb         & 0xFF;
                out.setPixel(x, y, (a << 24) | (b << 16) | (gC << 8) | r);
            }
        }
        return out;
    }
}