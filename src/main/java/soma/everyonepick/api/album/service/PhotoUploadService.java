package soma.everyonepick.api.album.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import soma.everyonepick.api.album.entity.GroupAlbum;
import soma.everyonepick.api.album.entity.Photo;
import soma.everyonepick.api.album.repository.PhotoRepository;
import soma.everyonepick.api.core.component.FileNameGenerator;
import soma.everyonepick.api.core.component.FileUploader;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoUploadService {
    public static final String DASH = "/";

    private final FileNameGenerator fileNameGenerator;
    private final FileUploader fileUploader;
    private final PhotoRepository photoRepository;

    /**
     * Multipart File 들을 업로드하고, Photo들을 생성한다.
     *
     * @param imageFiles 요청으로 들어온 Multipart File List
     * @return 저장 결과 Photo 리스트
     */
    @Transactional
    public List<Photo> uploadPhotos(List<MultipartFile> imageFiles, GroupAlbum groupAlbum) {

        final String ALBUM_ID = groupAlbum.getTitle() + '_' + groupAlbum.getId().toString();

        List<Photo> photos = new ArrayList<>();

        for (MultipartFile imageFile : imageFiles) {
            String generatedFileName = fileNameGenerator.generate(imageFile.getOriginalFilename());
            generatedFileName = ALBUM_ID + DASH + generatedFileName;
            String downloadableUrl = fileUploader.uploadMultiPartFile(imageFile, generatedFileName);

            photos.add(Photo.builder()
                    .photoUrl(downloadableUrl)
                    .groupAlbum(groupAlbum)
                    .build());
        }
        photos = photoRepository.saveAllAndFlush(photos);

        return photos;
    }
}
