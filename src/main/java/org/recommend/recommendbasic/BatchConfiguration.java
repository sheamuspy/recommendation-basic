package org.recommend.recommendbasic;


import org.recommend.recommendbasic.engine.Rater;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by sheamus on 8/18/2017.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public MongoTemplate mongoTemplate;

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<IRater> reader(){
        FlatFileItemReader<IRater> reader = new FlatFileItemReader<IRater>();
        reader.setResource(new ClassPathResource("rating-sample.csv"));
        reader.setLineMapper(new DefaultLineMapper<IRater>() {{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[]{"user", "item", "rating"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<IRater>(){{
                setTargetType(IRater.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<IRater, Rater> processor(){
        return new RaterItemProcessor();
    }

    @Bean
    public ItemWriter<Rater> writer(){
        MongoItemWriter<Rater> writer = new MongoItemWriter<Rater>();
//        try{
//
//            writer.setTemplate(mongoTemplate());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        writer.setCollection("rater");
        writer.setTemplate(mongoTemplate);
        writer.setCollection("r");
        return writer;
    }
    //end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job job(JobCompletionNotificationListener listener){
        return jobBuilderFactory.get("myjob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<IRater, Rater> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    // end::jobstep[]
/*
    @Bean
    private MongoTemplate mongoTemplate() throws Exception{
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    private MongoDbFactory mongoDbFactory() throws Exception{
        return new SimpleMongoDbFactory(new MongoClient(), "test");
    }
    */

}
