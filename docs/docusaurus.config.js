/** @type {import('@docusaurus/types').DocusaurusConfig} */
module.exports = {
  title: "Geolib",
  tagline:
    "Android helper libraries for geolocation, places, animating polyline and markering",
  url: "https://utsmannn.github.io",
  baseUrl: "/geolib/",
  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",
  favicon: "img/icon/geolib.ico",
  organizationName: "utsmannn", // Usually your GitHub org/user name.
  projectName: "geolib", // Usually your repo name.
  themeConfig: {
    colorMode: {
      defaultMode: "dark",
      disableSwitch: true,
    },
    prism: {
      additionalLanguages: ["kotlin", "groovy", "java"],
    },
    navbar: {
      title: "Home",
      logo: {
        alt: "Geolib",
        src: "img/icon/geolib.svg",
      },
      items: [
        {
          href: "https://github.com/utsmannn/geolib/releases",
          label: "v2.4.0",
          position: "right",
        },
        {
          href: "https://github.com/utsmannn/geolib",
          label: "GitHub Repository",
          position: "right",
        },
      ],
    },
    footer: {
      style: "dark",
      links: [
        {
          title: "Other Android Library",
          items: [
            {
              label: "Painless Paging Library",
              href: "https://github.com/utsmannn/painless-paging-library",
            },
            {
              label: "Networkism",
              href: "https://github.com/utsmannn/networkism",
            },
            {
              label: "And more on github...",
              href: "https://github.com/utsmannn",
            },
          ],
        },
        {
          title: "Socials",
          items: [
            {
              label: "GitHub",
              href: "https://github.com/utsmannn",
            },
            {
              label: "Linkedin",
              href: "https://www.linkedin.com/in/utsmannn/",
            },
            {
              label: "Youtube",
              href: "https://www.youtube.com/c/utsmannnkoding",
            },
          ],
        },
        {
          title: "Business Contact",
          items: [
            {
              label: "Email",
              href: "mailto:utsmannn@gmail.com",
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Muhammad Utsman. Built with Docusaurus.`,
    },
  },
  presets: [
    [
      "@docusaurus/preset-classic",
      {
        docs: {
          sidebarPath: require.resolve("./sidebars.js"),
          // Please change this to your repo.
          editUrl: "https://github.com/utsmannn/geolib/blob/master/docs/",
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      },
    ],
  ],
};
