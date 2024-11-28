/*
 * Copyright 2024 Google LLC
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

package com.google.firebase.example.dataconnect.gradle.tasks

import com.google.firebase.example.dataconnect.gradle.DataConnectGradleException
import com.google.firebase.example.dataconnect.gradle.providers.OperatingSystem

public class NodeJsPaths(
    public val downloadUrl: String,
    public val downloadFileName: String,
    public val shasumsUrl: String,
    public val shasumsFileName: String
) {
    public companion object
}

public fun NodeJsPaths.Companion.from(
    nodeJsVersion: String,
    operatingSystemType: OperatingSystem.Type,
    operatingSystemArchitecture: OperatingSystem.Architecture
): NodeJsPaths {
    val calculator = NodeJsPathsCalculator(nodeJsVersion, operatingSystemType, operatingSystemArchitecture)
    return NodeJsPaths(
        downloadUrl = calculator.downloadUrl(),
        downloadFileName = calculator.downloadFileName(),
        shasumsUrl = calculator.shasumsDownloadUrl(),
        shasumsFileName = calculator.shasumsDownloadFileName()
    )
}

private class NodeJsPathsCalculator(
    val nodeJsVersion: String,
    val operatingSystemType: OperatingSystem.Type,
    val operatingSystemArchitecture: OperatingSystem.Architecture
)

private fun NodeJsPathsCalculator.downloadUrlPrefix(): String = "https://nodejs.org/dist/v$nodeJsVersion"

/**
 * The URL to download the Node.js binary distribution.
 *
 * Here are some examples:
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-darwin-arm64.tar.xz
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-darwin-x64.tar.xz
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-linux-arm64.tar.xz
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-linux-armv7l.tar.xz
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-linux-x64.tar.xz
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-win-arm64.7z
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-win-x64.7z
 * * https://nodejs.org/dist/v20.9.0/node-v20.9.0-win-x86.7z
 */
private fun NodeJsPathsCalculator.downloadUrl(): String {
    val downloadUrlPrefix = downloadUrlPrefix()
    val downloadFileName = downloadFileName()
    return "$downloadUrlPrefix/$downloadFileName"
}

@Suppress("UnusedReceiverParameter")
private fun NodeJsPathsCalculator.shasumsDownloadFileName(): String = "SHASUMS256.txt.asc"

private fun NodeJsPathsCalculator.shasumsDownloadUrl(): String {
    val downloadUrlPrefix = downloadUrlPrefix()
    val downloadFileName = shasumsDownloadFileName()
    return "$downloadUrlPrefix/$downloadFileName"
}

/**
 * The file name of the download for the Node.js binary distribution.
 *
 * Here are some examples:
 * * node-v20.9.0-darwin-arm64.tar.xz
 * * node-v20.9.0-darwin-x64.tar.xz
 * * node-v20.9.0-linux-arm64.tar.xz
 * * node-v20.9.0-linux-armv7l.tar.xz
 * * node-v20.9.0-linux-x64.tar.xz
 * * node-v20.9.0-win-arm64.7z
 * * node-v20.9.0-win-x64.7z
 * * node-v20.9.0-win-x86.7z
 */
private fun NodeJsPathsCalculator.downloadFileName(): String {
    val fileExtension: String = when (operatingSystemType) {
        OperatingSystem.Type.Windows -> "7z"
        OperatingSystem.Type.MacOS -> "tar.xz"
        OperatingSystem.Type.Linux -> "tar.xz"
        else -> throw DataConnectGradleException(
            "unable to determine Node.js download file extension " +
                "for operating system type: $operatingSystemType " +
                "(error code ead53smf45)"
        )
    }

    val downloadFileNameBase = downloadFileNameBase()
    return "$downloadFileNameBase.$fileExtension"
}

/**
 * The base file name of the download for the Node.js binary distribution;
 * that is, the file name without the ".7z" or ".tar.xz" extension.
 *
 * Here are some examples:
 * * node-v20.9.0-darwin-arm64
 * * node-v20.9.0-darwin-x64
 * * node-v20.9.0-linux-arm64
 * * node-v20.9.0-linux-armv7l
 * * node-v20.9.0-linux-x64
 * * node-v20.9.0-win-arm64
 * * node-v20.9.0-win-x64
 * * node-v20.9.0-win-x86
 */
private fun NodeJsPathsCalculator.downloadFileNameBase(): String {
    val osType: String = when (operatingSystemType) {
        OperatingSystem.Type.Windows -> "win"
        OperatingSystem.Type.MacOS -> "darwin"
        OperatingSystem.Type.Linux -> "linux"
        else -> throw DataConnectGradleException(
            "unable to determine Node.js download base file name " +
                "for operating system type: $operatingSystemType " +
                "(error code m2grw3h7xz)"
        )
    }

    val osArch: String = when (operatingSystemArchitecture) {
        OperatingSystem.Architecture.Arm64 -> "arm64"
        OperatingSystem.Architecture.ArmV7 -> "armv7l"
        OperatingSystem.Architecture.X86 -> "x86"
        OperatingSystem.Architecture.X86_64 -> "x64"
    }

    return "node-v$nodeJsVersion-$osType-$osArch"
}