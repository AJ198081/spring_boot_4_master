package dev.aj.accounts.setup;

public enum AustralianBanks {
    CBA("06"),
    ANZ("01"),
    NAB("08"),
    Westpac("03");

    private final String bsb;

    AustralianBanks(String bsb) {
        this.bsb = bsb;
    }

    public String getBankBsb() {
        return bsb;
    }
}
