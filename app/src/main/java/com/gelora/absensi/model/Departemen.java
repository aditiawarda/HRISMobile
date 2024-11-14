package com.gelora.absensi.model;

import java.util.List;

public class Departemen {
    private String departemen;
    private List<Sales> sales;

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public List<Sales> getSales() {
        return sales;
    }

    public void setSales(List<Sales> sales) {
        this.sales = sales;
    }

    public static class Sales {
        private String nama_sales;
        private String foto;
        private String pending;
        private String in_process;
        private String complete;
        private String total;

        // Getters and setters

        public String getNama_sales() {
            return nama_sales;
        }

        public void setNama_sales(String nama_sales) {
            this.nama_sales = nama_sales;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public String getIn_process() {
            return in_process;
        }

        public void setIn_process(String in_process) {
            this.in_process = in_process;
        }

        public String getComplete() {
            return complete;
        }

        public void setComplete(String complete) {
            this.complete = complete;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}

