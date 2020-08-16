firewall {
    all-ping enable
    broadcast-ping disable
    ipv6-name WANv6_IN {
        default-action drop
        description "WAN inbound traffic forwarded to LAN"
        enable-default-log
        rule 10 {
            action accept
            description "Allow established/related sessions"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
    }
    ipv6-name WANv6_LOCAL {
        default-action drop
        description "WAN inbound traffic to the router"
        enable-default-log
        rule 10 {
            action accept
            description "Allow established/related sessions"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
        rule 30 {
            action accept
            description "Allow IPv6 icmp"
            protocol ipv6-icmp
        }
        rule 40 {
            action accept
            description "allow dhcpv6"
            destination {
                port 546
            }
            protocol udp
            source {
                port 547
            }
        }
    }
    ipv6-receive-redirects disable
    ipv6-src-route disable
    ip-src-route disable
    log-martians enable
    name WAN_IN {
        default-action drop
        description "WAN to internal"
        rule 10 {
            action accept
            description "Allow established/related"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
    }
    name WAN_LOCAL {
        default-action drop
        description "WAN to router"
        rule 10 {
            action accept
            description "Allow established/related"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
        rule 30 {
            action accept
            description uptimerobot.com
            icmp {
                type 8
            }
            protocol icmp
            source {
                address 69.162.124.224/28
            }
        }
        rule 31 {
            action accept
            description uptimerobot.com
            icmp {
                type 8
            }
            protocol icmp
            source {
                address 63.143.42.240/28
            }
        }
        rule 32 {
            action accept
            description uptimerobot.com
            icmp {
                type 8
            }
            protocol icmp
            source {
                address "216.245.221.80/28 "
            }
        }
    }
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
}
interfaces {
    ethernet eth0 {
        description NAS
        duplex auto
        speed auto
    }
    ethernet eth1 {
        description "Lauris desk"
        duplex auto
        speed auto
    }
    ethernet eth2 {
        description "Bobs desk"
        duplex auto
        speed auto
    }
    ethernet eth3 {
        description Wi-Fi
        duplex auto
        speed auto
    }
    ethernet eth4 {
        address dhcp
        address dhcpv6
        description Internet
        duplex auto
        firewall {
            in {
                ipv6-name WANv6_IN
                name WAN_IN
            }
            local {
                ipv6-name WANv6_LOCAL
                name WAN_LOCAL
            }
        }
        poe {
            output off
        }
        speed auto
    }
    loopback lo {
    }
    switch switch0 {
        address 192.168.1.1/24
        description Local
        dhcpv6-pd {
            rapid-commit enable
        }
        ipv6 {
            dup-addr-detect-transmits 1
        }
        mtu 1500
        switch-port {
            interface eth0 {
            }
            interface eth1 {
            }
            interface eth2 {
            }
            interface eth3 {
            }
            vlan-aware disable
        }
    }
}
port-forward {
    auto-firewall enable
    hairpin-nat disable
    lan-interface switch0
    wan-interface eth4
}
service {
    dhcp-server {
        disabled false
        hostfile-update disable
        shared-network-name LAN {
            authoritative enable
            subnet 192.168.1.0/24 {
                default-router 192.168.1.1
                dns-server 192.168.1.1
                domain-name sutterfields.us
                lease 86400
                ntp-server 192.168.1.1
                start 192.168.1.38 {
                    stop 192.168.1.243
                }
                static-mapping Canon-TR8520 {
                    ip-address 192.168.1.157
                    mac-address 00:bb:c1:bf:5a:75
                }
                time-offset 28800
                time-server 192.168.1.1
            }
        }
        static-arp disable
        use-dnsmasq disable
    }
    dns {
        forwarding {
            cache-size 8192
            listen-on switch0
            listen-on eth3
            listen-on eth2
            listen-on eth1
            listen-on eth0
            name-server 1.1.1.1
            name-server 1.0.0.1
            name-server 2606:4700:4700::1111
            name-server 2606:4700:4700::1001
            system
        }
    }
    gui {
        http-port 80
        https-port 443
        older-ciphers enable
    }
    nat {
        rule 5010 {
            description "masquerade for WAN"
            outbound-interface eth4
            type masquerade
        }
    }
    ssh {
        port 22
        protocol-version v2
    }
    unms {
        disable
    }
    upnp {
        listen-on eth0 {
            outbound-interface eth4
        }
        listen-on eth1 {
            outbound-interface eth4
        }
        listen-on eth2 {
            outbound-interface eth4
        }
        listen-on eth3 {
            outbound-interface eth4
        }
        listen-on switch0 {
            outbound-interface eth4
        }
    }
}
system {
    domain-name sutterfields.us
    domain-search {
    }
    host-name gw
    ipv6 {
    }
    login {
        user ubnt {
            authentication {
                encrypted-password $1$zKNoUbAo$gomzUbYvgyUMcD436Wo66.
            }
            level admin
        }
    }
    name-server 1.1.1.1
    name-server 1.0.0.1
    name-server 2606:4700:4700::1111
    name-server 2606:4700:4700::1001
    ntp {
        server 0.us.pool.ntp.org {
        }
        server 1.us.pool.ntp.org {
        }
        server 2.us.pool.ntp.org {
        }
        server 3.us.pool.ntp.org {
        }
        server us.pool.ntp.org {
        }
    }
    offload {
        hwnat enable
        ipsec enable
    }
    package {
        repository stretch {
            components "main contrib free non-free"
            distribution stretch
            password ""
            url http://http.us.debian.org/debian
            username ""
        }
    }
    static-host-mapping {
        host-name gw {
            alias gw.sutterfields.us
            inet 192.168.1.1
        }
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone UTC
    traffic-analysis {
        dpi enable
        export enable
        signature-update {
            update-hour 3
        }
    }
}


/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "config-management@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@5:nat@3:qos@1:quagga@2:suspend@1:system@4:ubnt-pptp@1:ubnt-udapi-server@1:ubnt-unms@1:ubnt-util@1:vrrp@1:vyatta-netflow@1:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: v2.0.8-hotfix.1.5278088.200305.1641 */
