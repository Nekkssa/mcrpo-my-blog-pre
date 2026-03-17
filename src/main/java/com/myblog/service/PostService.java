package com.myblog.service;

import com.myblog.dto.CreatePostRequest;
import com.myblog.dto.PostListResponse;
import com.myblog.dto.UpdatePostRequest;
import com.myblog.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

public interface PostService {
    PostListResponse getPosts(String search, int pageNumber, int pageSize);
    Optional<Post> getPostById(Long id);
    Post createPost(CreatePostRequest request);
    Post updatePost(Long id, UpdatePostRequest request);
    void deletePost(Long id);
    int incrementLikes(Long id);
    int decrementLikes(Long id);
    void saveImage(Long postId, byte[] imageData, String contentType);
    Optional<byte[]> getImage(Long postId);
    Optional<String> getImageContentType(Long postId);
}

@SpringBootTest  
@AutoConfigureMockMvc  
public class PostServiceTest {

    @Autowired  
    private MockMvc mockMvc;

    @Autowired  
    private PostService postService;

    @Autowired  
    private PostDao postDao;

    @Test  
    public void testCreatePost() throws Exception {
        CreatePostRequest request = new CreatePostRequest("Title", "Content", List.of("tag1", "tag2"));

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Title\", \"text\": \"Content\", \"tags\": [\"tag1\", \"tag2\"]}"))
                .andExpect(status().isCreated());

        // Дополнительные проверки  
        Post createdPost = postDao.findById(1L).orElse(null);
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTitle()).isEqualTo("Title");
    }
}

