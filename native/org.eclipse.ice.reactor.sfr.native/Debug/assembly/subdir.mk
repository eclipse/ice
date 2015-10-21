################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../assembly/PinAssembly.cpp \
../assembly/ReflectorAssembly.cpp \
../assembly/Ring.cpp \
../assembly/SFRAssembly.cpp \
../assembly/SFRPin.cpp \
../assembly/SFRRod.cpp 

OBJS += \
./assembly/PinAssembly.o \
./assembly/ReflectorAssembly.o \
./assembly/Ring.o \
./assembly/SFRAssembly.o \
./assembly/SFRPin.o \
./assembly/SFRRod.o 

CPP_DEPS += \
./assembly/PinAssembly.d \
./assembly/ReflectorAssembly.d \
./assembly/Ring.d \
./assembly/SFRAssembly.d \
./assembly/SFRPin.d \
./assembly/SFRRod.d 


# Each subdirectory must supply rules for building sources it contributes
assembly/%.o: ../assembly/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


