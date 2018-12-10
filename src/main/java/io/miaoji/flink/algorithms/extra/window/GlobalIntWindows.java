package io.miaoji.flink.algorithms.extra.window;

/**
 * 当前用户 : wing。
 * 当前日期 : 2018/12/10。
 * 包名 : io.miaoji.flink.algorithms.extra.window。
 * 当前时间 : 12:00 AM。
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.flink.annotation.Internal;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

import java.util.Collection;
import java.util.Collections;

/**
 * A {@link WindowAssigner} that assigns all elements to the same {@link GlobalWindow}.
 *
 * <p>Use this if you want to use a {@link Trigger} and
 * {@link org.apache.flink.streaming.api.windowing.evictors.Evictor} to do flexible, policy based
 * windows.
 */
@PublicEvolving
public class GlobalIntWindows extends WindowAssigner<Integer, GlobalWindow> {
    private static final long serialVersionUID = 1L;

    private GlobalIntWindows() {}

    @Override
    public Collection<GlobalWindow> assignWindows(Integer element, long timestamp, WindowAssignerContext context) {
        return Collections.singletonList(GlobalWindow.get());
    }

    @Override
    public Trigger<Integer, GlobalWindow> getDefaultTrigger(StreamExecutionEnvironment env) {
        return new GlobalIntWindows.NeverTrigger();
    }

    @Override
    public String toString() {
        return "GlobalIntWindows()";
    }

    /**
     * Creates a new {@code GlobalIntWindows} {@link WindowAssigner} that assigns
     * all elements to the same {@link GlobalWindow}.
     *
     * @return The global window policy.
     */
    public static GlobalIntWindows create() {
        return new GlobalIntWindows();
    }

    /**
     * A trigger that never fires, as default Trigger for GlobalIntWindows.
     */
    @Internal
    public static class NeverTrigger extends Trigger<Integer, GlobalWindow> {
        private static final long serialVersionUID = 1L;

        @Override
        public TriggerResult onElement(Integer element, long timestamp, GlobalWindow window, TriggerContext ctx) {
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onEventTime(long time, GlobalWindow window, TriggerContext ctx) {
            return TriggerResult.CONTINUE;
        }

        @Override
        public TriggerResult onProcessingTime(long time, GlobalWindow window, TriggerContext ctx) {
            return TriggerResult.CONTINUE;
        }

        @Override
        public void clear(GlobalWindow window, TriggerContext ctx) throws Exception {}

        @Override
        public void onMerge(GlobalWindow window, OnMergeContext ctx) {
        }
    }

    @Override
    public TypeSerializer<GlobalWindow> getWindowSerializer(ExecutionConfig executionConfig) {
        return new GlobalWindow.Serializer();
    }

    @Override
    public boolean isEventTime() {
        return false;
    }
}

