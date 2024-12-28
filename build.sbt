/*
 * Copyright 2023 Ossum Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.sbtidea.Keys.IntelliJPlatform
import sbt.Keys.*
import sbtbuildinfo.BuildInfoOption.{BuildTime, ToMap}
import com.ossuminc.sbt.OssumIncPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport.buildInfoKeys
import sbtbuildinfo.BuildInfoPlugin.autoImport.buildInfoOptions

enablePlugins(OssumIncPlugin)

Global / onChangedBuildSource := ReloadOnSourceChanges
(Global / excludeLintKeys) ++=
  Set(buildInfoPackage, buildInfoKeys, buildInfoOptions, mainClass, maintainer,
    intellijAttachSources)


lazy val root = Root("riddl-idea-plugin", "ossuminc", "com.ossuminc.riddl.idea", startYr=2023)
  .configure(With.noPublishing, With.git, With.dynver)
  .aggregate(plugin)

lazy val plugin = Module("plugin", "riddl-idea-plugin")
  .enablePlugins(SbtIdeaPlugin)
  .configure(With.typical, With.riddl("1.0.0-RC1"))
  .configure(With.packagingUniversal("reid@ossuminc.com","riddl-idea-plugin",
    "A plugin to support easier editing of RIDDL models in IntelliJ", "")
  )
  .settings(
    scalaVersion := "3.4.3",
    // scalacOptions := scala2_13_Options,
    intellijPluginName := "riddl-idea-plugin",
    intellijBuild      := "242.x",
    intellijPlatform   := IntelliJPlatform.IdeaCommunity,
    intellijPlugins    += "com.intellij.java".toPlugin,
    intellijPlugins    += "com.intellij.properties".toPlugin,
    intellijAttachSources := true,
    Compile / javacOptions ++= "--release" :: "17" :: Nil,
    libraryDependencies ++= Seq(
      "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.5" withSources()
    ),
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Test / unmanagedResourceDirectories    += baseDirectory.value / "testResources",
    buildInfoOptions := Seq(ToMap, BuildTime),
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      normalizedName,
      description,
      homepage,
      startYear,
      organization,
      organizationName,
      organizationHomepage,
      version,
      scalaVersion,
      sbtVersion
    )
  )

val scala2_13_Options = Seq(
  "-target:21",
  // "-Ypatmat-exhaust-depth 40", Zinc can't handle this :(
  "-Xsource:3",
  "-Wdead-code",
  "-deprecation",
  "-feature",
  "-Werror",
  "-Wunused:imports", // Warn if an import selector is not referenced.
  "-Wunused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:explicits", // Warn if an explicit parameter is unused.
  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:params", // Enable -Wunused:explicits,implicits.
  "-Xlint:nonlocal-return", // A return statement used an exception for flow control.
  "-Xlint:implicit-not-found", // Check @implicitNotFound and @implicitAmbiguous messages.
  "-Xlint:serial", // @SerialVersionUID on traits and non-serializable classes.
  "-Xlint:valpattern", // Enable pattern checks in val definitions.
  "-Xlint:eta-zero", // Warn on eta-expansion (rather than auto-application) of zero-ary method.
  "-Xlint:eta-sam", // Warn on eta-expansion to meet a Java-defined functional
  // interface that is not explicitly annotated with @FunctionalInterface.
  "-Xlint:deprecation" // Enable linted deprecations.
)
