package com.snek.engineersbliss.network.overlay_data.handlers;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;




public class RailInputDataHandler {
    private static boolean recording = false;
    private static BlockPos signalSourcePos = null;


    public static void startRecording() {
        recording = true;
        signalSourcePos = null;
    }
    public static void stopRecording() {
        recording = false;
    }


    public static boolean isRecording() {
        return recording;
    }


    public static @Nullable BlockPos getRecordedSignalSourcePos(){
        return signalSourcePos;
    }


    public static void setSignalSourcePos(final BlockPos pos) {
        signalSourcePos = pos;
    }
}
