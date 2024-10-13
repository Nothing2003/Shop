package rj.com.store.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for managing image services in cloud storage.
 */
public interface ImageServiceInCloud {

    /**
     * Uploads an image to the cloud storage.
     *
     * @param image the image file to be uploaded
     * @return the public ID or URL of the uploaded image
     * @throws IllegalArgumentException if the image is empty or null
     */
    String uploadImage(MultipartFile image);

    /**
     * Retrieves the public URL for an image using its public ID.
     *
     * @param publicID the public ID of the image stored in the cloud
     * @return the publicly accessible URL of the image
     * @throws IllegalArgumentException if the public ID is null or empty
     */
    String getURLFromPublicId(String publicID);

    /**
     * Deletes an image from cloud storage based on its public ID.
     *
     * @param publicId the public ID of the image to be deleted
     * @return a confirmation message or status of the deletion
     * @throws IllegalArgumentException if the public ID is null or empty
     */
    String deleteImage(String publicId);
}
