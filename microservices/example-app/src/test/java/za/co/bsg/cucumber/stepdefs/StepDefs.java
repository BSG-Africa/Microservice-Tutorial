package za.co.bsg.cucumber.stepdefs;

import za.co.bsg.ExampleApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ExampleApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
