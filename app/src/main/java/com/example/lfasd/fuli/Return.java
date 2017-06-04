package com.example.lfasd.fuli;


/**
 * Created by LFasd on 2017/5/25.
 */

public class Return {

    private boolean error;
    private Result[] results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("error is " + error + "\n\n");

        for (int i = 0; i < results.length; i++) {
            sb.append("result" + (i + 1) + "\n");
            sb.append("\n_id = " + results[i].get_id());
            sb.append("\ncreateAt = " + results[i].getCreatedAt());
            sb.append("\ndesc = " + results[i].getDesc());
            sb.append("\nimages = ");
            if (results[i].getImages() != null && results[i].getImages().length > 0) {
                for (String image : results[i].getImages()) {
                    sb.append(image + "\n");
                }
            } else {
                sb.append("null\n");
            }
            sb.append("publishedAt = " + results[i].getPublishedAt());
            sb.append("\nsource = " + results[i].getSource());
            sb.append("\ntype = " + results[i].getType());
            sb.append("\nurl = " + results[i].getUrl());
            sb.append("\nused = " + results[i].isUsed());
            sb.append("\nwho = " + results[i].getWho() + "\n\n");
        }

        return sb.toString();
    }
}
