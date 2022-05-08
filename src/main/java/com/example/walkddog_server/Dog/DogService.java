package com.example.walkddog_server.Dog;

import com.example.walkddog_server.Config.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DogService {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcDogInsert;
    private final Path uploadPath;


    @Autowired
    public DogService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcDogInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("dog")
                .usingColumns("dog_name", "dog_age", "dog_gender", "dog_owner").usingGeneratedKeyColumns("dog_id");
        this.uploadPath = Paths.get(Constants.uploadPath);
    }

    public List<Dog> getAllDogs() {
        return jdbcTemplate.query("select * from dog", (rs, rowNum) -> new Dog(
                rs.getInt("dog_id"),
                rs.getString("dog_name"),
                rs.getInt("dog_age"),
                rs.getString("dog_owner")
        ));
    }

    public List<Dog> getDogsByOwner(String owner) {
        return jdbcTemplate.query("select * from dog where dog_owner = ?", (rs, rowNum) -> new Dog(
                rs.getInt("dog_id"),
                rs.getString("dog_name"),
                rs.getInt("dog_age"),
                rs.getString("dog_owner")
        ), owner);
    }


    public int insertDog(Dog dog) {
        return simpleJdbcDogInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("dog_name", dog.getName())
                .addValue("dog_age", dog.getAge())
                .addValue("dog_gender", dog.getGender())
                .addValue("dog_owner", dog.getOwner())).intValue();
    }

    public int deleteDog(long dog_id) {
        deleteImage(dog_id);
        return jdbcTemplate.update("delete from dog where dog_id = ? ", dog_id);
    }

    public void uploadImage(MultipartFile file, long dog_id) throws IOException {
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdirs();
        }
        if (!file.isEmpty()) {
            String[] list = file.getOriginalFilename().split("\\.");
            String extension = list[list.length - 1];
            try {
                file.transferTo(uploadPath.resolve(dog_id + "." + extension));
            } catch (InvalidPathException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deleteImage(long id) {
        File file = findFile(id);
        if (file != null) {
            return file.delete();
        } else
            return false;
    }

    public File findFile(long id) {
        String[] extensions = {"jpg", "png", "jpeg"};
        String defaultFileName = "default.jpg";
        for (String extension : extensions) {
            File file = uploadPath.resolve(id + "." + extension).toFile();
            if (file.exists()) {
                return file;
            }
        }
        return uploadPath.resolve(defaultFileName).toFile();
    }

    public File findFile(String id) {
        return findFile(Long.parseLong(id));
    }

}
