package com.hzyi.jplab;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @goal run
 */
@Mojo(name = "build")
public class JplabBuildMojo extends AbstractMojo {

  /**
   * @parameter
   */
  private String sim;

  /**
   * @parameter 
   */
  private String stage;

  public void execute() throws MojoExecutionException, MojoFailureException {
    if (sim == null) {
      throw new MojoFailureException("Sim has to be provided.");	
    }
  	getLog().info("");
  	System.out.println("Done.");
  }
}