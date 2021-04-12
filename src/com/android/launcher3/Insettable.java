/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.graphics.Rect;

/**
 * Allows the implementing {@link View} to not draw underneath system bars.
 * e.g., notification bar on top and home key area on the bottom.
 * 状态栏铺满接口 在launcher.xml 布局中加入的子控件 只要实现它就能生效
 */
public interface Insettable {

    void setInsets(Rect insets);
}