package com.example.demo.controller;

import com.example.demo.commands.FeedCommandHandler;
import com.example.demo.commands.PlayCommandHandler;
import com.example.demo.commands.SleepCommandHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/commands")
public class CommandController {
    private final FeedCommandHandler feedCommandHandler;
    private final PlayCommandHandler playCommandHandler;
    private final SleepCommandHandler sleepCommandHandler;

    public CommandController(FeedCommandHandler feedCommandHandler, SleepCommandHandler sleepCommandHandler, PlayCommandHandler playCommandHandler) {
        this.feedCommandHandler = feedCommandHandler;
        this.sleepCommandHandler = sleepCommandHandler;
        this.playCommandHandler = playCommandHandler;
    }

    @PostMapping("/feed")
    public void feed(){
        feedCommandHandler.handle();
    }

    @PostMapping("/sleep")
    public void sleep(){
        sleepCommandHandler.handle();
    }

    @PostMapping("/play")
    public void play(){
        playCommandHandler.handle();
    }
}
