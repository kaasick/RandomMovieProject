package io.delivery.randommoviepicker.model;


// To set a range of how many top movies to randomly choose from

public enum MovieRange {
    TOP_100(100),
    TOP_250(250),
    TOP_500(500),
    TOP_1000(1000);

    private final int limit;

    MovieRange(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
