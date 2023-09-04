package ru.practicum.shareit.item.mapper;

import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper
@Generated
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto toItemDto(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "lastBooking", source = "lastBooking", qualifiedByName = "toBookingOwnerDto")
    @Mapping(target = "nextBooking", source = "nextBooking", qualifiedByName = "toBookingOwnerDto")
    @Mapping(target = "comments", source = "comments")
    ItemOwnerDto toItemOwnerDto(Item item,
                                Booking lastBooking,
                                Booking nextBooking,
                                List<Comment> comments);

    @Named("toBookingOwnerDto")
     static BookingOwnerDto toBookingOwnerDto(Booking booking) {
        return BookingMapper.INSTANCE.toBookingOwnerDto(booking);
    }

    Item toItem(ItemDto itemDto);

    List<ItemDto> convertItemListToItemDTOList(List<Item> list);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);
}
