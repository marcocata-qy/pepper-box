package com.gslab.pepper.config.serialized;

import com.gslab.pepper.model.FieldExpressionMapping;
import com.gslab.pepper.loadgen.BaseLoadGenerator;
import com.gslab.pepper.loadgen.impl.SerializedLoadGenerator;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The SerializedConfigElement custom jmeter config element. This class acts as serialized object feeder to jmeter java sampler, it includes serialized load generator which takes input class and its property expressions and generates messages.
 *
 * @Author Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0.1
 * @since 05/06/2019
 */
public class SerializedConfigElement extends ConfigTestElement implements TestBean, LoopIterationListener {

    // clase whose objects will be generated by load generator
    private String className;

    // class fields and their mapping with functions
    private List<FieldExpressionMapping> objProperties;

    // message placeholder key
    private String placeHolder;

    // serialized object load generator
    private BaseLoadGenerator generator = null;

    private static final Logger log = LoggerFactory.getLogger(SerializedLoadGenerator.class);

    /**
     * For every JMeter sample, iterationStart method gets invoked, it initializes load generator and for each iteration sets new message as JMeter variable
     *
     * @param loopIterationEvent
     */
    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

        try {
            //Check if load generator is instantiated
            if (generator == null) {

                //instantiate serialized load generator
                generator = new SerializedLoadGenerator(className, objProperties);

            }

            //For ever iteration put message in jmeter variables
            JMeterVariables variables = JMeterContextService.getContext().getVariables();
            variables.putObject(placeHolder, generator.nextMessage());
        } catch (Exception e) {
            log.error("Failed to create PlaintTextLoadGenerator instance", e);
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<FieldExpressionMapping> getObjProperties() {
        return objProperties;
    }

    public void setObjProperties(List<FieldExpressionMapping> objProperties) {
        this.objProperties = objProperties;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

}
