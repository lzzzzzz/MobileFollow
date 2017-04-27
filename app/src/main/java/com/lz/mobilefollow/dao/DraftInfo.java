package com.lz.mobilefollow.dao;

import org.java_websocket.drafts.Draft;

public class DraftInfo {

        private final String draftName;
        private final Draft draft;

        public DraftInfo(String draftName, Draft draft) {
            this.draftName = draftName;
            this.draft = draft;
        }

    public String getDraftName() {
        return draftName;
    }

    public Draft getDraft() {
        return draft;
    }

    @Override
        public String toString() {
            return draftName;
        }
    }