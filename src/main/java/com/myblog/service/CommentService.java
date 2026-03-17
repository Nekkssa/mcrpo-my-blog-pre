package com.myblog.service;


import com.myblog.dto.CreateCommentRequest;
import com.myblog.dto.UpdateCommentRequest;
import com.myblog.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getCommentsByPostId(Long postId);
    Optional<Comment> getCommentById(Long commentId);
    Comment createComment(CreateCommentRequest request);
    Comment updateComment(Long commentId, UpdateCommentRequest request);
    void deleteComment(Long commentId);
}

@Service  
public class CommentServiceImpl implements CommentService {
    // Внедрение зависимостей (например, CommentDao)
    private final CommentDao commentDao;

    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    // Реализация методов интерфейса  
    @Override  
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentDao.findByPostId(postId);
    }

    @Override  
    public Optional<Comment> getCommentById(Long commentId) {
        return commentDao.findById(commentId);
    }

    @Override  
    public Comment createComment(CreateCommentRequest request) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setPostId(request.getPostId());
        return commentDao.create(comment);
    }

    @SpringBootTest  
    public class CommentServiceTest {

        @Autowired  
        private CommentService commentService;

        @MockBean  
        private CommentDao commentDao;

        @Test  
        public void testCreateComment() {
            CreateCommentRequest request = new CreateCommentRequest("Test comment", 1L);
            Comment createdComment = new Comment();
            createdComment.setId(1L);
            createdComment.setText(request.getText());
            createdComment.setPostId(request.getPostId());

            when(commentDao.create(any(Comment.class))).thenReturn(createdComment);

            Comment result = commentService.createComment(request);

            assertThat(result).isNotNull();
            assertThat(result.getText()).isEqualTo("Test comment");
            verify(commentDao, times(1)).create(any(Comment.class));
        }

        @Test  
        public void testGetCommentById() {
            Comment comment = new Comment();
            comment.setId(1L);
            comment.setText("Test comment");
            comment.setPostId(1L);

            when(commentDao.findById(1L)).thenReturn(Optional.of(comment));

            Optional<Comment> result = commentService.getCommentById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getText()).isEqualTo("Test comment");
            verify(commentDao, times(1)).findById(1L);
        }
    }