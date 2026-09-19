
# Contributing

<br>

```txt
dev-feature  >  dev  >  multiversion  >  master
```

All PRs should target `dev` or other feature branches.<br>
Merging from `dev` to `multiversion` to `master` is the owner's job.

<br>

## Master branch

The `master` branch always contains the latest stable release.<br>
Releases and version tags are created exclusively on `master`.

<br>

## New features

The `dev` branch contains generic fixes, improvements, and features for versions that have yet to be released.
<br>
<br>
New features should be developed in **`Minecraft 26.1`** on dedicated branches called `dev-<feature_name>`,
or in a single `dev-<collection_name>` in case of systems containing many related features.<br>
Once finished and tested, they can be merged into `dev` or other feature branches.

<br>

> Some feature branches (e.g. `dev-creative-tweaks`) are permanent collectors for a category of related features.<br>
> Smaller sub-features can branch off these and PR back into them before the collector branch itself merges into dev.

> New features are developed in `26.1` to ensure they can be implemented in all supported versions.<br>
> 26.1 is used as baseline because it has the most restrictive API. If a feature works there, it can work everywhere else.

<br>

## Multiversion support

Complete versions merge from `dev` into `multiversion`, where they are tested to ensure everything that was added is compatible with **ALL** supported Minecraft versions.<br>
Once compatibility is ensured, `multiversion` can merge into `master`.

The `multiversion` branch is also used to update the mod when new Minecraft versions are released.<br>
This is done **without** going through `dev` first.

<br>

## Other branches and tags

Temporary branches can be added for complex features, fixes, or extensive refactoring/rewrites.<br>
These should be merged into `dev` and deleted once they aren't needed anymore.
<br>
<br>
Non-version tags can be added to any permanent branch to mark significant milestones or important changes to the codebase.

<br>

## AI-generated PRs

Using models for debugging and prototyping is fine, but please, write the final code yourself.<br>
Test it manually and ensure it's optimized, readable, and maintainable.<br>
You should understand what it does very well and be able to edit it if needed.
<br>
<br>
Fully AI-generated PRs will be rejected **without review**.
<br>
<br>
AI-generated assets (e.g. textures, sounds, videos) are **not allowed**.
