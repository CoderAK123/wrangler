package io.cdap.wrangler.utils.testing;

import io.cdap.wrangler.api.RecipeException;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.executor.RecipePipelineExecutor;
import io.cdap.wrangler.parser.GrammarBasedParser;
import io.cdap.wrangler.proto.Contexts;
import io.cdap.wrangler.registry.CompositeDirectiveRegistry;
import io.cdap.wrangler.registry.SystemDirectiveRegistry;

import java.util.List;

public class RecipeTester {
    public static List<Row> run(String[] recipe, List<Row> rows) throws RecipeException {
        String recipeText = String.join("\n", recipe);
        GrammarBasedParser parser = new GrammarBasedParser(
            Contexts.SYSTEM, 
            recipeText,
            new CompositeDirectiveRegistry(SystemDirectiveRegistry.INSTANCE)
        );
        RecipePipelineExecutor executor = new RecipePipelineExecutor(parser, null);
        return executor.execute(rows);
    }
}
