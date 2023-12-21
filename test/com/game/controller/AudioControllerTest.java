package com.game.controller;

import org.junit.Before;
import org.junit.Test;

public class AudioControllerTest {

    @Before
    public void setup() {
        AudioController.loadMusic();
    }

    /*
     * Boundary Value Testing - Audio Volume
     */

    @Test(expected = IllegalArgumentException.class)
    public void setMusicVolume_throwsExceptionWhenVolumeIsLessThanZero() {
        AudioController.setMusicVolume(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMusicVolume_throwsExceptionWhenVolumeIsGreaterThanHundred() {
        AudioController.setMusicVolume(101);
    }

    @Test
    public void setMusicVolume_doesNotThrowExceptionWhenVolumeIsWithinValidRange() {
        AudioController.setMusicVolume(1);
        AudioController.setMusicVolume(50);
        AudioController.setMusicVolume(100);
    }

    /*
     * Boundary Value Testing - Sfx Volume
     */

    @Test(expected = IllegalArgumentException.class)
    public void setSfxVolume_throwsExceptionWhenVolumeIsLessThanZero() {
        AudioController.setSfxVolume(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setSfxVolume_throwsExceptionWhenVolumeIsGreaterThanHundred() {
        AudioController.setSfxVolume(101);
    }

    @Test
    public void setSfxVolume_doesNotThrowExceptionWhenVolumeIsWithinValidRange() {
        AudioController.setSfxVolume(1);
        AudioController.setSfxVolume(50);
        AudioController.setSfxVolume(100);
    }
}