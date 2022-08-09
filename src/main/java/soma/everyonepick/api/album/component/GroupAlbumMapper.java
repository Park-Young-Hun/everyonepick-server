package soma.everyonepick.api.album.component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import soma.everyonepick.api.album.dto.GroupAlbumReadDto;
import soma.everyonepick.api.album.dto.PhotoDto;
import soma.everyonepick.api.album.entity.GroupAlbum;
import soma.everyonepick.api.album.service.PhotoService;
import soma.everyonepick.api.album.service.UserGroupAlbumService;
import soma.everyonepick.api.user.component.UserMapper;
import soma.everyonepick.api.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class GroupAlbumMapper {
    @Autowired
    protected UserGroupAlbumService userGroupAlbumService;
    @Autowired
    protected PhotoService photoService;

    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected PhotoMapper photoMapper;

    @Mapping(target = "users", expression = "java(getMemberDtos(groupAlbum))")
    @Mapping(target = "photos", expression = "java(getPhotoDtos(groupAlbum))")
    public abstract GroupAlbumReadDto toReadDto(GroupAlbum groupAlbum);

    protected List<UserDto> getMemberDtos(GroupAlbum groupAlbum) {
        return userGroupAlbumService.getMembers(groupAlbum).stream()
                .map(s -> userMapper.toDto(s)).collect(Collectors.toList());
    }

    protected  List<PhotoDto> getPhotoDtos(GroupAlbum groupAlbum) {
        return photoService.getPhotos(groupAlbum).stream()
                .map(s -> photoMapper.toDto(s)).collect(Collectors.toList());
    }
}
