package com.stctest.anycompmarketplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stctest.anycompmarketplace.response.BaseResponse;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Properties;
import java.util.TimeZone;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/os")
    public BaseResponse<Map<String, Object>> osInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        
        try {
            systemInfo.put("name", System.getProperty("os.name"));
            systemInfo.put("version", System.getProperty("os.version"));
            systemInfo.put("arch", System.getProperty("os.arch"));
            systemInfo.put("user.name", System.getProperty("user.name"));
            systemInfo.put("user.timezone", TimeZone.getDefault().getID());
            systemInfo.put("user.country", System.getProperty("user.country"));
            systemInfo.put("user.language", System.getProperty("user.language"));
            systemInfo.put("current.time", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            // Add Linux-specific information
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                Map<String, String> linuxInfo = new HashMap<>();
                
                // Read Linux distribution info
                File osRelease = new File("/etc/os-release");
                if (osRelease.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(osRelease))) {
                        reader.lines().forEach(line -> {
                            String[] parts = line.split("=", 2);
                            if (parts.length == 2) {
                                String value = parts[1].replace("\"", "");
                                linuxInfo.put(parts[0].toLowerCase(), value);
                            }
                        });
                    }
                }

                // Get kernel version
                try {
                    Process process = Runtime.getRuntime().exec("uname -r");
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String kernelVersion = reader.readLine();
                        if (kernelVersion != null) {
                            linuxInfo.put("kernel_version", kernelVersion);
                        }
                    }
                } catch (Exception e) {
                    linuxInfo.put("kernel_version_error", e.getMessage());
                }

                // CPU Info from /proc/cpuinfo
                try {
                    File cpuinfo = new File("/proc/cpuinfo");
                    if (cpuinfo.exists()) {
                        Map<String, String> cpuDetails = new HashMap<>();
                        try (BufferedReader reader = new BufferedReader(new FileReader(cpuinfo))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(":", 2);
                                if (parts.length == 2) {
                                    String key = parts[0].trim();
                                    String value = parts[1].trim();
                                    if (key.equals("model name") || key.equals("cpu cores") || 
                                        key.equals("vendor_id") || key.equals("cpu MHz")) {
                                        cpuDetails.put(key.replace(" ", "_"), value);
                                    }
                                }
                            }
                        }
                        linuxInfo.put("cpu_details", cpuDetails.toString());
                    }
                } catch (Exception e) {
                    linuxInfo.put("cpu_info_error", e.getMessage());
                }

                systemInfo.put("linux_specific", linuxInfo);
            }
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }
        return new BaseResponse<>(systemInfo);
    }

    @GetMapping("/java")
    public BaseResponse<Map<String, Object>> javaInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        try {
            // Java Information
            systemInfo.put("version", System.getProperty("java.version"));
            systemInfo.put("vendor", System.getProperty("java.vendor"));
            systemInfo.put("home", System.getProperty("java.home"));
            systemInfo.put("class.version", System.getProperty("java.class.version"));
            systemInfo.put("compiler", System.getProperty("java.compiler", "Unknown"));
            systemInfo.put("vm.name", System.getProperty("java.vm.name"));
            systemInfo.put("vm.version", System.getProperty("java.vm.version"));
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }

        return new BaseResponse<>(systemInfo);
    }

    @GetMapping("/memory")
    public BaseResponse<Map<String, Object>> memoryInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        try {
            // Memory Information
            Runtime runtime = Runtime.getRuntime();
            systemInfo.put("total.memory", formatSize(runtime.totalMemory()));
            systemInfo.put("free.memory", formatSize(runtime.freeMemory()));
            systemInfo.put("used.memory", formatSize(runtime.totalMemory() - runtime.freeMemory()));
            systemInfo.put("max.memory", formatSize(runtime.maxMemory()));
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }

        return new BaseResponse<>(systemInfo);
    }

    @GetMapping("/cpu")
    public BaseResponse<Map<String, Object>> cpuInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        try {
            // CPU Information
            Runtime runtime = Runtime.getRuntime();
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            systemInfo.put("available.processors", String.valueOf(runtime.availableProcessors()));
            systemInfo.put("system.load.average", String.format("%.2f", osBean.getSystemLoadAverage()));
            systemInfo.put("system.arch", osBean.getArch());
            
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }
        return new BaseResponse<>(systemInfo);
    }

    @GetMapping("/disk")
    public BaseResponse<Map<String, Object>> diskInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        try {
            // Disk Information
            File[] roots = File.listRoots();
            Map<String, Map<String, String>> disksMap = new HashMap<>();
            for (File root : roots) {
                Map<String, String> diskDetails = new HashMap<>();
                diskDetails.put("total.space", formatSize(root.getTotalSpace()));
                diskDetails.put("free.space", formatSize(root.getFreeSpace()));
                diskDetails.put("usable.space", formatSize(root.getUsableSpace()));
                disksMap.put(root.getAbsolutePath(), diskDetails);
            }
            systemInfo.put("drives", disksMap);
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }
        return new BaseResponse<>(systemInfo);
    }

    @GetMapping("/network")
    public BaseResponse<Map<String, Object>> networkInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        try{
            // Network Information
            systemInfo.put("hostname", InetAddress.getLocalHost().getHostName());
            systemInfo.put("host.address", InetAddress.getLocalHost().getHostAddress());

            // Network Interfaces
            Map<String, Object> interfacesInfo = new HashMap<>();
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (ni.isUp() && !ni.isLoopback()) {
                    Map<String, Object> interfaceDetails = new HashMap<>();
                    interfaceDetails.put("display.name", ni.getDisplayName());
                    interfaceDetails.put("mtu", ni.getMTU());
                    interfaceDetails.put("is.virtual", ni.isVirtual());
                    interfaceDetails.put("supports.multicast", ni.supportsMulticast());
                    
                    // IP addresses
                    List<String> addresses = Collections.list(ni.getInetAddresses()).stream()
                            .map(InetAddress::getHostAddress)
                            .collect(Collectors.toList());
                    interfaceDetails.put("addresses", addresses);
                    
                    interfacesInfo.put(ni.getName(), interfaceDetails);
                }
            }
            systemInfo.put("interfaces", interfacesInfo);
            
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }
        return new BaseResponse<>(systemInfo);
    }

    @GetMapping("/systemProperties")
    public BaseResponse<Map<String, Object>> systemPropertiesInfo() {
        // System Properties
        Map<String, Object> systemInfo = new HashMap<>();
        try{
            Properties props = System.getProperties();
            for (String key : props.stringPropertyNames()) {
                systemInfo.put(key, System.getProperty(key));
            }
        } catch (Exception e) {
            systemInfo.put("error", "Error collecting some system information: " + e.getMessage());
        }
        return new BaseResponse<>(systemInfo);
    }

    private String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}