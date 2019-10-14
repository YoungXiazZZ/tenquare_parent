package entity;


import lombok.Data;

import java.util.List;

/**
 * Created by xiayang on 2019/9/18.
 */
@Data
public class PageResult<T> {
    private long total;

    private List<T> rows;

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}
