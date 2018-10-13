package com.github.zhangkaitao.shiro.chapter10.session.dao;

import com.github.zhangkaitao.shiro.chapter10.JdbcTemplateUtils;
import com.github.zhangkaitao.shiro.chapter10.SerializableUtils;
import java.io.Serializable;
import java.util.List;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-8
 * <p>Version: 1.0
 */
public class MySessionDAO extends CachingSessionDAO {

    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();

    private final Logger log = LoggerFactory.getLogger(MySessionDAO.class);

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        log.info("do create session: {}.", session.toString());
        String sql = "insert into sessions(id, session) values(?,?)";
        jdbcTemplate.update(sql, sessionId, SerializableUtils.serialize(session));
        return session.getId();
    }

    @Override
    protected void doUpdate(Session session) {
        log.info("do update session: {}.", session.toString());
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return; //如果会话过期/停止 没必要再更新了
        }
        String sql = "update sessions set session=? where id=?";
        jdbcTemplate.update(sql, SerializableUtils.serialize(session), session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        log.info("do delete session: {}.", session.toString());
        String sql = "delete from sessions where id=?";
        jdbcTemplate.update(sql, session.getId());
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String sql = "select session from sessions where id=?";
        List<String> sessionStrList = jdbcTemplate.queryForList(sql, String.class, sessionId);
        if (sessionStrList.size() == 0) {
            return null;
        }
        Session session = SerializableUtils.deserialize(sessionStrList.get(0));
        log.info("do read session: {}.", session.toString());
        return session;
    }
}
