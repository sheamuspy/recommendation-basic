package org.recommend.recommendbasic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


/**
 * Created by sheamus on 8/18/2017.
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final MongoTemplate mongoTemplate;

    @Autowired
    public JobCompletionNotificationListener(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("!!! JOB FINISHED! Time to verify the results");
        }else if(jobExecution.getStatus() == BatchStatus.FAILED){
            log.info("!!! JOB FAILED");
        }
        log.info("JOB: " +jobExecution.getStatus());
//        super.afterJob(jobExecution);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("JOB: " +jobExecution.getStatus());

        super.beforeJob(jobExecution);
    }
}
