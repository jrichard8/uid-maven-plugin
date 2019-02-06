package org.jrichard.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UidMojoTest {

    final UidMojo uidMojo = new UidMojo();

    @Before
    public void setuUp() {
        uidMojo.setGidPropertyName("user.uid");
        uidMojo.setUidPropertyName("user.gid");
    }

    @Test
    public void execute() throws MojoExecutionException {

        String uid = System.getProperty("user.uid");
        String gid = System.getProperty("user.gid");
        Assert.assertTrue(StringUtils.isEmpty(uid));
        Assert.assertTrue(StringUtils.isEmpty(gid));


        uidMojo.execute();

        uid = System.getProperty("user.uid");
        gid = System.getProperty("user.gid");
        Assert.assertFalse(StringUtils.isEmpty(gid));
        Assert.assertFalse(StringUtils.isEmpty(uid));

    }
}