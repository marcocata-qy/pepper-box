package com.gslab.pepper.config.plaintext;

import com.gslab.pepper.loadgen.BaseLoadGenerator;
import com.gslab.pepper.loadgen.impl.PlaintTextLoadGenerator;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The PlainTextConfigElement custom jmeter config element. This class acts as plain text feeder to jmeter java sampler, it includes plaintext load generator which takes input schema and generates messages.
 *
 * @Author  Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>, Marco Catalano <marco.catalano1992@gmail.com>
 * @Version 1.0.1
 * @since 05/06/2019
 */
public class PlainTextConfigElement extends ConfigTestElement implements TestBean, LoopIterationListener {

    private static final Logger log = LoggerFactory.getLogger(PlainTextConfigElement.class);

    //Input schema template could be json, xml, csv or custom plain text format
    private String jsonSchema;

    //Plaintext Load Generator
    private BaseLoadGenerator generator;

    //Message placeholder key
    private String placeHolder;


    /**
     * For every JMeter sample, iterationStart method gets invoked, it initializes load generator and for each iteration sets new message as JMeter variable
     *
     * @param loopIterationEvent
     */
    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

        //Check if load generator is instantiated
        if (generator == null) {

            try {

                //instantiate plaintext load generator
                generator = new PlaintTextLoadGenerator(getJsonSchema());

            } catch (Exception e) {
                log.error("Failed to create PlaintTextLoadGenerator instance", e);
            }

        }

        //For ever iteration put message in jmeter variables
        JMeterVariables variables = JMeterContextService.getContext().getVariables();
        variables.putObject(placeHolder, generator.nextMessage());
    }

    public String getJsonSchema() {
        return jsonSchema;
    }

    public void setJsonSchema(String jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    public BaseLoadGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(BaseLoadGenerator generator) {
        this.generator = generator;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

}
