package com.idgenerator.mode;

/**
 * 业务编码生成实体
 */
public class NumberGeneratorMode {

    /**
     * 业务编码
     */
    private String numberCode;

    /**
     * 业务编码描述
     */
    private String numberDesc;

    /**
     * 增长序列
     */
    private int step=1;

    /**
     * 编码格式 如：codePre{0}-{1} 仅支持两个占位符
     */
    private String numberFormat;

    /**
     * 编码模板，如：yyyyMM 目前仅支持日期
     */
    private String codeFormat="yyyyMMdd";

    /**
     * 增长位长度，不够前面补 leftPadChar
     */
    private int sequenceLength;

    /**
     * 序列长度不够时补位符
     */
    private String leftPadChar;

    /**
     * 起始序列号
     */
    private Long startSequence=1L;

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getNumberDesc() {
        return numberDesc;
    }

    public void setNumberDesc(String numberDesc) {
        this.numberDesc = numberDesc;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getCodeFormat() {
        return codeFormat;
    }

    public void setCodeFormat(String codeFormat) {
        this.codeFormat = codeFormat;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public String getLeftPadChar() {
        return leftPadChar;
    }

    public void setLeftPadChar(String leftPadChar) {
        this.leftPadChar = leftPadChar;
    }

    public Long getStartSequence() {
        return startSequence;
    }

    public void setStartSequence(Long startSequence) {
        this.startSequence = startSequence;
    }
}
