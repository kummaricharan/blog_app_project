package com.example.crudoperation.controller;

import com.example.crudoperation.entity.Comments;
import com.example.crudoperation.entity.Post;
import com.example.crudoperation.services.CommentService;
import com.example.crudoperation.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private CommentService commentService;
    private PostService postService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }


    @PostMapping("/save")
    public String saveComment(@RequestParam("postId") Long postId,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("comments") String comments) {
        Post post = postService.findById(postId);
        Comments comment = new Comments();
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(comments);
        comment.setPost(post);
        commentService.save(comment);
        return "redirect:/posts/showPost?postId=" + postId;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("commentId") Long commentId, Model model) {
        Comments comment = commentService.findById(commentId);
        model.addAttribute("comment", comment);
        return "posts/post-form-update";
    }

    @PostMapping("/update")
    public String updateComment(@ModelAttribute("comment") Comments comment) {
        commentService.save(comment);
        return "redirect:/posts/showPost?postId=" + comment.getPost().getId();
    }

    @PostMapping("/delete")
    public String deleteComment(@RequestParam("commentId") Long commentId) {
        Comments comment = commentService.findById(commentId);
        Long postId = comment.getPost().getId();
        commentService.deleteById(commentId);
        return "redirect:/posts/showPost?postId=" + postId;
    }
}
