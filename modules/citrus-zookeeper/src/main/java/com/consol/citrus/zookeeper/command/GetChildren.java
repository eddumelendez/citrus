/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.zookeeper.command;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.zookeeper.client.ZooClient;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Martin Maher
 * @since 2.5
 */
public class GetChildren extends AbstractZooCommand<ZooResponse> {

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(GetChildren.class);

    /**
     * Default constructor initializing the command name.
     */
    public GetChildren() {
        super("zookeeper:getChildren");
    }

    @Override
    public void execute(ZooClient zookeeperClient, TestContext context) {
        ZooResponse commandResult = new ZooResponse();
        setCommandResult(commandResult);

        String path = this.getParameter("path", context);

        try {
            List<String> children = zookeeperClient.getZooKeeperClient().getChildren(path, false);
            commandResult.setResponseParam("children", children);
        } catch (InterruptedException | KeeperException e) {
            throw new CitrusRuntimeException(e);
        }
        log.debug(getCommandResult().toString());
    }
}
