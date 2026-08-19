# ProGuard rules for SASSA ScamShield
# Add project specific rules here

# Hilt
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn dagger.**
-dontwarn com.google.dagger.**
-dontwarn com.squareup.inject.**
-dontwarn androidx.hilt.**
-dontwarn androidx.hilt.navigation.**
