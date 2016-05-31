package com.jaspersoft.jasperserver.dsa;

import com.jaspersoft.jasperserver.dsa.common.AppConfiguration;
import com.jaspersoft.jasperserver.dsa.common.ConsoleUtil;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategy;
import com.jaspersoft.jasperserver.dsa.initialization.InitializationStrategyFactory;
import com.jaspersoft.jasperserver.dsa.querexecution.FlatQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.MultiAxesQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.MultiLevelQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.ProvidedQueryExecutor;
import com.jaspersoft.jasperserver.dsa.querexecution.QueryExecutor;
import com.jaspersoft.jasperserver.dto.adhoc.query.ClientQuery;
import com.jaspersoft.jasperserver.dto.resources.domain.DataIslandsContainer;
import org.apache.log4j.Logger;

/**
 * <p/>
 * <p/>
 *
 * @author tetiana.iefimenko
 * @version $Id$
 * @see
 */
public class Application {
    private static Logger appLogger = Logger.getLogger(Application.class);
    private static Logger consoleLogger = Logger.getLogger("consoleLogger");
    private static final String ADHOC_VIEW_URI = "/public/Samples/Ad_Hoc_Views/01__Geographic_Results_by_Segment";
    private static final  String DOMAIN_URI = "/public/Samples/Domains/supermartDomain";

    public static void main(String[] args) {
        appLogger.info("Initialization of application");

        // Initialization and configuration of application
        consoleLogger.info("Choose way of configuration (file or manual) [f/m]: ");
        InitializationStrategy strategy = InitializationStrategyFactory.resolveStrategy(ConsoleUtil.readChar(new Character[]{'f', 'm'}));
        AppConfiguration configuration = strategy.initConfiguration();
        // Initialization server session
        configuration.initSession();
        DataIslandsContainer domainMetadata;
        //Execute provided query and save result dataset to file
        QueryExecutor providedQueryExecutor = new ProvidedQueryExecutor(configuration);
        domainMetadata = providedQueryExecutor.retrieveMetadata(ADHOC_VIEW_URI);
        ClientQuery clientQuery = providedQueryExecutor.buildQuery(domainMetadata);
        String providedResultData = providedQueryExecutor.executeQuery(clientQuery);
        providedQueryExecutor.saveQueryExecutionResults(providedResultData, "providedQuery.json");

        //Retrieve metadata, build and  execute flat query and save dataset to file
        QueryExecutor flatQueryExecutor = new FlatQueryExecutor(configuration);
        domainMetadata = flatQueryExecutor.retrieveMetadata(DOMAIN_URI);
        ClientQuery flatQuery = flatQueryExecutor.buildQuery(domainMetadata);
        String flatQueryResultData = flatQueryExecutor.executeQuery(flatQuery);
        flatQueryExecutor.saveQueryExecutionResults(flatQueryResultData, "flatQuery.json");

        //Retrieve metadata, build and  execute multi level query and save dataset to file
        QueryExecutor multiLevelQueryExecutor = new MultiLevelQueryExecutor(configuration);
        domainMetadata = multiLevelQueryExecutor.retrieveMetadata(DOMAIN_URI);
        ClientQuery multiLevelQuery = multiLevelQueryExecutor.buildQuery(domainMetadata);
        String multiLevelResultData = multiLevelQueryExecutor.executeQuery(multiLevelQuery);
        flatQueryExecutor.saveQueryExecutionResults(multiLevelResultData, "multiLevelQuery.json");

        //Retrieve metadata, build and  execute multi axes query and save dataset to file
        QueryExecutor multiAxesQueryExecutor = new MultiAxesQueryExecutor(configuration);
        domainMetadata = multiAxesQueryExecutor.retrieveMetadata(DOMAIN_URI);
        ClientQuery multiAxesClientQuery = multiAxesQueryExecutor.buildQuery(domainMetadata);
        String multiAxesResultData = multiAxesQueryExecutor.executeQuery(multiAxesClientQuery);
        multiAxesQueryExecutor.saveQueryExecutionResults(multiAxesResultData, "multiAxesQuery.json");

        //Logout on the server and stoop the application
        configuration.stopApplication();
    }
}
