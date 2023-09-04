package com.example.crudoperation.controller;

import com.example.crudoperation.entity.Post;
import com.example.crudoperation.entity.Tag;
import com.example.crudoperation.services.PostService;
import com.example.crudoperation.services.TagService;
import com.example.crudoperation.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {
    private PostService postService;
    private TagService tagService;
    private UserService userService;

    public PostController(PostService postService, TagService tagService,UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));
        Post post = new Post();
        if(isAuthor){
            String username = authentication.getName();
            post.setAuthor(username);
        }
        model.addAttribute("post", post);
        return "posts/post-form";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") Post post, @RequestParam("tagsInput") String tagsInput, Model model) {
        List<Tag> tags = new ArrayList<>();
        if(tagsInput.length()!=0){
            for (String string : tagsInput.split(",")) {
                String trim = string.trim();
                Tag orCreateTag = (Tag) tagService.findOrCreateTag(trim);
                tags.add(orCreateTag);
            }
            model.addAttribute("tags", tags);
        }
        post.setTags(tags);

        postService.save(post);

        return "redirect:/posts/showPost?postId=" + post.getId();
    }

    @GetMapping("/showPost")
    public String showPost(@RequestParam("postId") Long postId, Model model) {
        Post post = postService.findById(postId);
        model.addAttribute("post", post);
        return "posts/post-form-update";
    }
    @GetMapping("/showDraftPost")
    public String showDraftPost(@RequestParam("postId") Long postId, Model model,Principal principal) {
        String loggedInUser = principal.getName(); // principal used to retrieve logged in user username
        Post post = postService.findById(postId);
//        if(post!=null && post.getAuthor().equals(loggedInUser)){
//            model.addAttribute("post", post);
//            return "posts/draft_post_update";
//        }
//        else{
//            return "redirect:access-denied";
//        }
        model.addAttribute("post", post);
        return "posts/draft_post_update";
    }
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("postId") Long theId, Model model) {
        Post post = postService.findById(theId);
        model.addAttribute("post", post);
        String currentTags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));
        System.out.print(currentTags);
        model.addAttribute("tagsInput", currentTags);
        return "posts/post-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("postId") Long theId) {
        postService.deleteById(Math.toIntExact(theId));
        return "redirect:/posts/list";
    }

    @GetMapping("/search")
    public String searching(@RequestParam("search") String searchParam, Model model) {
        List<Post> posts = postService.Searching(searchParam);
        model.addAttribute("posts", posts);
        return "posts/list-post";
    }

    @GetMapping("/sortByParam")
    public String sortByParam(@RequestParam("sort") String searchParam, Model model) {
        List<Post> posts = postService.sortBy(searchParam);
        model.addAttribute("posts", posts);
        return "posts/list-post";
    }
    @RequestMapping("/draftPost")
    public String draftPost(Model model){
        List<Post> draftPost = postService.findAllDraftPost();
        model.addAttribute("posts",draftPost);
        return "posts/draft_post";
    }
    @GetMapping("/yourPosts")
    public String seeYourPost(Model model,Authentication authentication){
        List<Post> post = postService.findAllNotDraftPost();
        model.addAttribute("posts",post);
        return "posts/see_your_post";
    }
    @GetMapping("/draftPostOfAllAuthors")
    public String draftPostOfAllAuthors(Model model){
        List<Post> post = postService.findAllDraftPost();
        model.addAttribute("posts",post);
        return "posts/draft_post_all_authors";
    }
    @GetMapping("/list")
    public String listOfPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "search", required = false) String searchParam,
            @RequestParam(name = "sort", required = false) String sortParam,
            @RequestParam(name = "author", required = false) String[] selectedAuthors,
            @RequestParam(name = "tag", required = false) String[] selectedTags,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Principal principal,
            Model model) {
        Page<Post> postsPage;
        Pageable pageable;

        Set<String> authors = selectedAuthors != null ? new HashSet<>(Arrays.asList(selectedAuthors)) : new HashSet<>();
        Set<String> tags = selectedTags != null ? new HashSet<>(Arrays.asList(selectedTags)) : new HashSet<>();

        Set<String> filterAuthors = new HashSet<>();
        Set<String> filterTags = new HashSet<>();

        List<String> author = postService.getAllAuthors();
        List<String> tag = postService.getAllTags();

        if (sortParam != null) {
            Sort sort = Sort.by("publishedAt").ascending();
            if ("title".equals(sortParam)) {
                sort = Sort.by("title").ascending();
            } else if ("author".equals(sortParam)) {
                sort = Sort.by("author").ascending();
            }
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        }

        if (!authors.isEmpty() || !tags.isEmpty() || startDate != null || endDate != null) {
            postsPage = postService.resultWithFilterSearch(searchParam,authors, tags, startDate, endDate, pageable);
            if (searchParam != null) {
                List<Post> posts = postsPage.getContent();
                for (Post post : posts) {
                    filterAuthors.add(post.getAuthor());
                }
                for (Post post : posts) {
                    for (Tag filtertag : post.getTags()) {
                        filterTags.add(filtertag.getName());
                    }
                }
            }
            model.addAttribute("authors", filterAuthors);
            model.addAttribute("tags", filterTags);
        }
        else if (searchParam != null && ! searchParam.equals("")) {
            System.out.println(searchParam);
            postsPage = postService.searchPosts(searchParam, pageable);
            List<Post> posts = postsPage.getContent();
            for (Post post : posts) {
                filterAuthors.add(post.getAuthor());
            }
            for (Post post : posts) {
                for (Tag filtertag : post.getTags()) {
                    filterTags.add(filtertag.getName());
                }
            }
            model.addAttribute("authors", filterAuthors);
            model.addAttribute("tags", filterTags);
        }
        else {
            postsPage = postService.findAll(pageable);
            model.addAttribute("authors", author);
            model.addAttribute("tags", tag);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("hasUserRole", authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        } else {
            model.addAttribute("isAuthenticated", false);
            model.addAttribute("hasUserRole", false);
        }

        model.addAttribute("postsPage", postsPage);
        model.addAttribute("selectedAuthors", authors);
        model.addAttribute("selectedTags", tags);
        model.addAttribute("searchParam", searchParam);
        model.addAttribute("sortParam", sortParam);

        StringBuilder paginationUrl = new StringBuilder("/posts/list?")
                .append("size=").append(size);
        System.out.println(paginationUrl);
        if (searchParam != null) {
            paginationUrl.append("&search=").append(URLEncoder.encode(searchParam, StandardCharsets.UTF_8));
        }
        if (sortParam != null) {
            paginationUrl.append("&sort=").append(sortParam);
        }
        if (selectedAuthors != null) {
            for (String author_data : selectedAuthors) {
                paginationUrl.append("&author=").append(author_data);
            }
        }
        if (selectedTags != null) {
            for (String tag_data : selectedTags) {
                paginationUrl.append("&tag=").append(tag_data);
            }
        }
        if (startDate != null) {
            paginationUrl.append("&startDate=").append(startDate.toString());
        }
        if (endDate != null) {
            paginationUrl.append("&endDate=").append(endDate.toString());
        }
        model.addAttribute("paginationUrl", paginationUrl.toString());

        return "posts/list-post";
    }
}
