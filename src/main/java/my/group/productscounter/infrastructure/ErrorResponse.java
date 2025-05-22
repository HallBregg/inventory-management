package my.group.productscounter.infrastructure;

record ErrorResponse(
        String code,
        String message,
        Object details) {
}
