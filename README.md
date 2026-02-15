# ShahNexus PDF Viewer (16KB Page Size Compatible)

A lightweight, high-performance Android PDF viewer library built with Android's native `PdfRenderer` API. This library is specifically designed to be **16KB page size compliant**, meeting the mandatory requirements for **Android 15+ (API 35)** devices and Google Play submissions starting November 2025.

## 🚀 Why this library?

Traditional PDF libraries like `barteksc/AndroidPdfViewer` rely on native C++ libraries (Pdfium) compiled with 4KB page alignment. On Android 15+ devices with 16KB page sizes, these libraries will cause apps to crash or be rejected by Google Play.

**Manus PDF Viewer** solves this by using the system's native `PdfRenderer`, which is:
- ✅ **100% 16KB Compatible**: Guaranteed to work on Android 15, 16, and beyond.
- ✅ **Zero Native Dependencies**: No `.so` files to manage or align.
- ✅ **Ultra Lightweight**: Tiny library size compared to Pdfium-based alternatives.
- ✅ **High Performance**: Leverages hardware-accelerated system rendering.

## 📦 Installation

Add the JitPack repository to your `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.shahnexus42:pdf-viewer-16kb:1.0.2'
}
```

## 📱 Basic Usage

### 1. Add PDFView to your layout

```xml
<com.manus.pdfviewer.PDFView
    android:id="@+id/pdfView"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### 2. Load a PDF

```java
PDFView pdfView = findViewById(R.id.pdfView);

// From Assets
pdfView.fromAsset("sample.pdf");

// From File
pdfView.fromFile(new File("/path/to/document.pdf"));
```

## ✨ Features

- [x] **16KB Page Size Support** for Android 15+
- [x] Pinch-to-Zoom & Double-tap zoom
- [x] Smooth Scrolling & Fling gestures
- [x] Horizontal & Vertical swipe support
- [x] Lightweight & Fast
- [x] Easy migration from barteksc/AndroidPdfViewer

## 🛠 Android 15 (16KB) Configuration

To ensure your app is fully compliant with Android 15's 16KB page size requirement, add this to your `app/build.gradle`:

```gradle
android {
    packagingOptions {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
