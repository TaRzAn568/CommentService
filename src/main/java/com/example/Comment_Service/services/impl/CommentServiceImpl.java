package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.CommentDto;
import com.example.Comment_Service.dto.ReplyDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.model.Comment;
import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.Reply;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.CommentRepository;
import com.example.Comment_Service.repository.PostRepository;
import com.example.Comment_Service.repository.ReplyRepository;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;



    public CommentDto commentToDto(Comment comment){
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        commentDto.setPost_Id(comment.getPost().getId());
        commentDto.setUser_Id(comment.getUser().getId());
        commentDto.setReplies(comment.getReplies().stream().map(this::replyToDto).collect(Collectors.toList()));
        return commentDto;
    }
    public Comment dtoToComment(CommentDto commentDto){
        return modelMapper.map(commentDto, Comment.class);
    }
    public ReplyDto replyToDto(Reply reply){

        ReplyDto replyDto =  modelMapper.map(reply, ReplyDto.class);
        replyDto.setUser_Id(reply.getUser().getId());
        replyDto.setParent_comment_Id(reply.getParentComment().getId());
        return replyDto;
    }
    public Reply dtoToReply(ReplyDto replyDto){
        return modelMapper.map(replyDto, Reply.class);
    }


    @Override
    public List<CommentDto> getAllTopLevelComments(Long postId) {
       List<Comment> comments =  commentRepository.findByPostId(postId);
        return comments.stream().map(this::commentToDto).toList();
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment =  commentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", id));
        return  commentToDto(comment);
    }

    @Override
    public CommentDto addComment(CommentDto commentDto) {
        User user = userRepository.findById(commentDto.getUser_Id()).orElseThrow(()-> new ResourceNotFoundException("User", "id", commentDto.getUser_Id()));
        Post post = postRepository.findById(commentDto.getPost_Id()).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", commentDto.getPost_Id()));
        Comment comment = dtoToComment(commentDto);
        comment.setPost(post);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);
        return commentToDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto newCommentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", id));
        Comment newComment = dtoToComment(newCommentDto);
        comment.setText(newComment.getText());
        Comment savedComment = commentRepository.save(comment);
        return commentToDto(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", id));
        commentRepository.delete(comment);
    }

    @Override
    public ReplyDto addReplyToComment(ReplyDto replyDto, Long parentCommentId) {
        User user = userRepository.findById(replyDto.getUser_Id()).orElseThrow(()-> new ResourceNotFoundException("User", "id", replyDto.getUser_Id()));
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "id", parentCommentId));
        Reply reply = dtoToReply(replyDto);
        reply.setParentComment(parentComment);
        reply.setUser(user);
        return replyToDto( replyRepository.save(reply));
    }
    @Override
    public ReplyDto addReplyToReply(ReplyDto replyDto, Long replyId) {
        User user = userRepository.findById(replyDto.getUser_Id()).orElseThrow(()-> new ResourceNotFoundException("User", "id", replyDto.getUser_Id()));
        Reply parentReply = replyRepository.findById(replyId).orElseThrow(()-> new ResourceNotFoundException("Reply", "id", replyId));
        Reply reply = dtoToReply(replyDto);
        //need to change
        reply.setParentComment(parentReply.getParentComment());
        reply.setUser(user);
        return replyToDto( replyRepository.save(reply));
    }

    @Override
    public List<ReplyDto> getRepliesToComment(Long parentCommentId) {
        List<Reply> replies = replyRepository.findRepliesByCommentId(parentCommentId);
        return replies.stream().map(this::replyToDto).collect(Collectors.toList());
    }
}
