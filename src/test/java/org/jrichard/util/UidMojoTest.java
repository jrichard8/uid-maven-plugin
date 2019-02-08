package org.jrichard.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UidMojoTest {


    private UidMojo uidMojo = new UidMojo();

    private MavenProject mavenProject = new MavenProject();

    @Before
    public void setuUp() {
        uidMojo.setGidPropertyName("user.uid");
        uidMojo.setUidPropertyName("user.gid");
        uidMojo.setProject(mavenProject);
    }

    @Test
    public void execute() throws MojoExecutionException {

        String uid = mavenProject.getProperties().getProperty("user.uid");
        String gid = mavenProject.getProperties().getProperty("user.gid");
        Assert.assertTrue(StringUtils.isEmpty(uid));
        Assert.assertTrue(StringUtils.isEmpty(gid));

        uidMojo.execute();

        uid = mavenProject.getProperties().getProperty("user.uid");
        gid = mavenProject.getProperties().getProperty("user.gid");
        Assert.assertFalse(StringUtils.isEmpty(gid));
        Assert.assertFalse(StringUtils.isEmpty(uid));

    }
}