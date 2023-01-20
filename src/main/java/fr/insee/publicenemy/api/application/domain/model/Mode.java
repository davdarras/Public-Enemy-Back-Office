package fr.insee.publicenemy.api.application.domain.model;

public enum Mode {
    CAWI(true),
    CATI(true),
    CAPI(true),
    PAPI(false);

    private final boolean isWebMode;

    Mode(boolean isWebMode) {
        this.isWebMode = isWebMode;
    }

    public boolean isWebMode() {
        return isWebMode;
    }
}
