package handler;

import common.annotation.CanalTable;
import common.bean.CanalHandler;
import entity.Demo;

/**
 * @author gaozijie
 * @since 2024-01-19
 */
@CanalTable(database = "demo", table = "demo")
public class DemoCanalHandler extends CanalHandler<Demo> {

    @Override
    public void insert(Demo data) {

    }

    @Override
    public void update(Demo beforeData, Demo afterData) {

    }

    @Override
    public void delete(Demo data) {

    }

}
