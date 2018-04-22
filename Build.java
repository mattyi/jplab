import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal query
 */
@Mojo(name = "Build")
public class Build extends AbstractMojo {

  /**
   * @parameter
   */
  private String sim;

  public void execute() throws MojoExecutionException {

  }
}