package ru.practicum.shareit.exception.operationAccess;

public class ItemOperationAccessException extends OperationAccessException {
    public ItemOperationAccessException(Long id) {
        super("User with ID = " + id + " not the owner of the thing and therefore cannot change it.");
    }
}
