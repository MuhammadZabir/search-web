package com.zabir.searchweb.cucumber.stepdefs;

import com.zabir.searchweb.SearchWebApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = SearchWebApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
