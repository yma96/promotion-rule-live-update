package org.commonjava.indy.promotion.validation;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class RuleFormatValidation {
    public static void main( String[] args ) {
        File rulesPath = new File( "promote/rules/" );
        File ruleSetsPath = new File( "promote/rule-sets/" );

        File[] rules = rulesPath.listFiles();
        File[] ruleSets = ruleSetsPath.listFiles();

        if ( rules != null )
        {
            for ( File file : rules )
            {
                System.out.println( ">>>Read file: " + file.getName() );
                if ( file.isFile() && file.getName().matches( ".*\\.groovy" ) )
                {
                    continue;
                }
                else
                {
                    System.err.println( ">>>Rule File: " + file.getName() + " is not a valid groovy file." );
                    System.exit( 1 );
                }
            }
            System.out.println( ">>>Rules format validation passed" );
        }

        if ( ruleSets != null )
        {
            for ( File file : ruleSets )
            {
                System.out.println( ">>>Read file: " + file.getName() );
                try
                {
                    String json = FileUtils.readFileToString( file );
                    if ( json.isEmpty() )
                    {
                        System.err.println( ">>>RuleSet File: " + file.getName() + " is EMPTY." );
                        System.exit( 1 );
                    }
                    try
                    {
                        new JsonParser().parse( json );
                    }
                    catch ( JsonParseException e )
                    {
                        System.err.println( ">>>RuleSet File: " + file.getName() + " is not a valid json file." );
                        System.exit( 1 );
                    }
                }
                catch ( IOException e )
                {
                    System.err.println( ">>>RuleSet File: " + file.getName() + " can not be readed." );
                    System.exit( 1 );
                }
            }
            System.out.println( ">>>Rule sets format validation passed" );
        }
    }
}
