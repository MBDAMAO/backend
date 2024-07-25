package com.damao.controller;

import com.damao.pojo.entity.Video;
import com.damao.pojo.vo.FeedItemVO;
import com.damao.pojo.vo.HomeFeedVO;
import com.damao.pojo.vo.VideoRankVO;
import com.damao.result.Result;
import com.damao.service.VideoRecommendService;
import com.damao.service.VideoResourceService;
import com.damao.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    VideoResourceService videoResourceService;

    @Autowired
    VideoRecommendService videoRecommendService;

    @PostMapping("/publish")
    public Result<String> publish(@RequestBody Video video) {
        Long vid = videoService.publish(video);
        return Result.success("视频已发布，请等待审核。视频id：" + vid);
    }

    @GetMapping("/get/{vid}")
    public Result<Video> getVideoById(@PathVariable Long vid) {
        Video video = videoService.getVideoById(vid);
        return Result.success(video);
    }

    @GetMapping("/getResource/{vid}")
    public Result<String> getContentById(@PathVariable Long vid) {
        String videoUrl = videoResourceService.getUrlByVid(vid);
        return Result.success(videoUrl);
    }

    @GetMapping("/getDailyRank")
    public Result<List<VideoRankVO>> getDailyTopTenViewVideos() {
        return Result.success(videoService.getDailyTopTenViewVideos());
    }

    @GetMapping("/randomFeed")
    public Result<List<Video>> recommend() {
        List<Video> res = videoRecommendService.get();
        return Result.success(res);
    }

    @GetMapping("/feed")
    public Result<List<FeedItemVO>> feed() {
        List<FeedItemVO> res = videoRecommendService.feed();
        return Result.success(res);
    }

    @GetMapping("/home_feed")
    public Result<List<HomeFeedVO>> homeFeed() {
        List<HomeFeedVO> res = videoRecommendService.homeFeed();
        return Result.success(res);
    }

    @GetMapping("/recommend/{vid}")
    public Result<List<Video>> recommendSimilar(@PathVariable Long vid) {
        List<Video> res = videoRecommendService.recommendSimilar(vid);
        return Result.success(res);
    }

    @GetMapping("/getVideoLikesCount/{vid}")
    public Result<?> getVideoLikesCount(@PathVariable Long vid) {
        return Result.success(videoService.getVideoLikesCount(vid));
    }

    /**
     * 对视频进行点赞
     */
    @PutMapping("/like/{vid}/{status}")
    public Result<?> likeVideo(@PathVariable Long vid, @PathVariable Integer status) {
        videoService.saveLikeRelationUser(vid, status);
        return Result.success("点赞成功");
    }

    @GetMapping("/getLikesStatus/{vid}")
    public Result<?> getLikesStatus(@PathVariable Long vid) {
        return Result.success(videoService.getLikesStatus(vid));
    }

    @GetMapping("/collections")
    public Result<?> collections() {
        videoService.getMyCollections();
        return Result.success("点赞成功");
    }
}
