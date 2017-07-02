package com.kivimango.coffeecommander.view.dialog;

public final class CopyDialogResult {

    private final boolean shouldOverwrite;
    private final boolean shouldKeepOriginalFileDates;

    public CopyDialogResult(boolean overwrite, boolean preserve) {
        this.shouldOverwrite = overwrite;
        this.shouldKeepOriginalFileDates = preserve;
    }

    public boolean shouldOverwrite() {
        return shouldOverwrite;
    }

    public boolean shouldKeepOriginalFileDates() {
        return shouldKeepOriginalFileDates;
    }
}
