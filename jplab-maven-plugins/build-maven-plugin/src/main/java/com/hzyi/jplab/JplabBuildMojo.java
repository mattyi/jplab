package com.hzyi.jplab;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * @goal run
 */
@Mojo(name = "build")
public class JplabBuildMojo extends AbstractMojo {

  @Parameter (property = "sim", defaultValue = "DumApplication")
  private String sim;

  @Parameter( defaultValue = "${project}", readonly = true )
  protected MavenProject project;

  /**
   * @parameter 
   */
  private String stage;

  public void execute() throws MojoExecutionException, MojoFailureException {
    System.out.println(sim);
    try {
      //String pathName = "";
      String pathName = "com.hzyi.jplab.dum.DumApplication";
      Class<?> simApplicationClass = Class.forName(pathName);
      String[] param = null;
      simApplicationClass.getMethod("main", String[].class).invoke(null, null);
    } catch(Exception e) {
      System.out.println(e);
    }
    // executeMojo(
    //     plugin(
    //         groupId("org.codehaus.mojo"),
    //         artifactId("exec-maven-plugin"),
    //         version("2.0")
    //     ),
    //     goal("exec:java"),
    //     configuration(
    //         element(name("mainClass"), "com.hzyi.jplab.dum.DumApplication")
    //     ),
    //     executionEnvironment(
    //         mavenProject,
    //         mavenSession,
    //         pluginManager
    //     )
    // );
  }
}