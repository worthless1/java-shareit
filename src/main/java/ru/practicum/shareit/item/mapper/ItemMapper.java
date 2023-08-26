package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOwner;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "available", source = "item.available")
    ItemDto toItemDto(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "lastBooking", source = "lastBooking", qualifiedByName = "BookingToBookingDtoOwner")
    @Mapping(target = "nextBooking", source = "nextBooking", qualifiedByName = "BookingToBookingDtoOwner")
    @Mapping(target = "comments", source = "comments")
    ItemDtoOwner toItemDtoOwner(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments);

    @Named("BookingToBookingDtoOwner")
    static BookingShortDto toBookingDtoOwner(Booking booking) {
        return BookingMapper.INSTANCE.toBookingDtoOwner(booking);
    }

    Item toItem(ItemDto itemDto);

    List<ItemDto> convertItemListToItemDTOList(List<Item> list);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);
}
