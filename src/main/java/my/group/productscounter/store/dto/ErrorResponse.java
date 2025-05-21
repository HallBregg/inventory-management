package my.group.productscounter.store.dto;

public record ErrorResponse(
        String code,
        String message,
        Object details) {
}
