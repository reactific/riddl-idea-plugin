/*
 * Copyright 2019 Reactific Software LLC
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
import sbt.Keys.scalaVersion
import sbtbuildinfo.BuildInfoOption.{BuildTime, ToMap}

ThisBuild / maintainer := "reid@reactific.com"
ThisBuild / organizationName := "Reactific Software LLC"
ThisBuild / startYear := Some(2019)
ThisBuild / licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

Global / onChangedBuildSource := ReloadOnSourceChanges
(Global / excludeLintKeys) ++=
  Set(buildInfoPackage, buildInfoKeys, buildInfoOptions, mainClass, maintainer,
    intellijAttachSources)

ThisBuild / versionScheme := Option("semver-spec")
ThisBuild / dynverVTagPrefix := false

// NEVER  SET  THIS: version := "0.1"
// IT IS HANDLED BY: sbt-dynver
ThisBuild / dynverSeparator := "-"
ThisBuild / organization := "com.reactific"
ThisBuild / scalaVersion := "2.13.7"
buildInfoOptions := Seq(ToMap, BuildTime)
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

lazy val scala2_13_Options = Seq(
  "-target:17",
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

lazy val `riddl-idea-plugin` = project.in(file("."))
  .enablePlugins(SbtIdeaPlugin)
  .settings(
    ThisBuild / intellijPluginName := "riddl-idea-plugin",
    ThisBuild / intellijBuild      := "213.6461.79",
    ThisBuild / intellijPlatform   := IntelliJPlatform.IdeaCommunity,
    intellijPlugins       += "com.intellij.properties".toPlugin,
    ThisBuild / intellijAttachSources := true,
    Compile / javacOptions ++= "--release" :: "17" :: Nil,
    libraryDependencies ++= Seq(
      "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.5" withSources()
    ),
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Test / unmanagedResourceDirectories    += baseDirectory.value / "testResources",
    libraryDependencies ++= Seq()

  )
