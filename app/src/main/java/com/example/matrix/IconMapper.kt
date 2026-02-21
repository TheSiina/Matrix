package com.example.matrix

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// آیکون های لیست بانک مشاغل
fun getIconFromName(name: String): ImageVector {
    return when (name) {
        "Android" -> Icons.Default.Android
        "TrendingUp" -> Icons.AutoMirrored.Filled.TrendingUp
        "Brush" -> Icons.Default.Brush
        "Search" -> Icons.Default.Search
        "Gavel" -> Icons.Default.Gavel
        "Memory" -> Icons.Default.Memory
        "Campaign" -> Icons.Default.Campaign
        "Share" -> Icons.Default.Share
        "AdsClick" -> Icons.Default.AdsClick
        "Code" -> Icons.Default.Code
        "Assessment" -> Icons.Default.Assessment
        "Calculate" -> Icons.Default.Calculate
        "MonetizationOn" -> Icons.Default.MonetizationOn
        "AccountBalance" -> Icons.Default.AccountBalance
        "AdminPanelSettings" -> Icons.Default.AdminPanelSettings
        "SettingsSuggest" -> Icons.Default.SettingsSuggest
        "Build" -> Icons.Default.Build
        "VideoCameraBack" -> Icons.Default.VideoCameraBack
        "Mic" -> Icons.Default.Mic
        "EditNote" -> Icons.Default.EditNote
        "Animation" -> Icons.Default.Animation
        "Science" -> Icons.Default.Science
        "Palette" -> Icons.Default.Palette
        "Category" -> Icons.Default.Category
        "Draw" -> Icons.Default.Draw
        "Leaderboard" -> Icons.Default.Leaderboard
        "ManageSearch" -> Icons.Default.ManageSearch
        "Stars" -> Icons.Default.Stars
        "PriceCheck" -> Icons.Default.PriceCheck
        "Storefront" -> Icons.Default.Storefront
        "Handshake" -> Icons.Default.Handshake
        "Inventory" -> Icons.Default.Inventory
        "RocketLaunch" -> Icons.Default.RocketLaunch
        "Face" -> Icons.Default.Face
        "MedicalServices" -> Icons.Default.MedicalServices
        "Engineering" -> Icons.Default.Engineering
        "FitnessCenter" -> Icons.Default.FitnessCenter
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "PrecisionManufacturing" -> Icons.Default.PrecisionManufacturing
        "SettingsInputComponent" -> Icons.Default.SettingsInputComponent
        "FactCheck" -> Icons.Default.FactCheck
        "WbSunny" -> Icons.Default.WbSunny
        "Air" -> Icons.Default.Air
        "BatterySaver" -> Icons.Default.BatterySaver
        "Lightbulb" -> Icons.Default.Lightbulb
        "VerifiedUser" -> Icons.Default.VerifiedUser
        "ReceiptLong" -> Icons.Default.ReceiptLong
        "Shield" -> Icons.Default.Shield
        "SelfImprovement" -> Icons.Default.SelfImprovement
        "HomeWork" -> Icons.Default.HomeWork
        "RestaurantMenu" -> Icons.Default.RestaurantMenu
        "Gamepad" -> Icons.Default.Gamepad
        "CurrencyBitcoin" -> Icons.Default.CurrencyBitcoin
        "SmartToy" -> Icons.Default.SmartToy
        "Medication" -> Icons.Default.Medication
        "AutoFixHigh" -> Icons.Default.AutoFixHigh
        "RealEstateAgent" -> Icons.Default.RealEstateAgent
        "Agriculture" -> Icons.Default.Agriculture
        "RecordVoiceOver" -> Icons.Default.RecordVoiceOver
        "Checkroom" -> Icons.Default.Checkroom
        "CastForEducation" -> Icons.Default.CastForEducation
        "BusinessCenter" -> Icons.Default.BusinessCenter
        else -> Icons.Default.Work
    }
}